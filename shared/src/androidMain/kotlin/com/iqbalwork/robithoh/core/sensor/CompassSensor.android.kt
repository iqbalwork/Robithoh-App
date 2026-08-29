package com.iqbalwork.robithoh.core.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
actual fun rememberCompassSensor(): CompassState {
    val context = LocalContext.current
    var compassState by remember { mutableStateOf(CompassState()) }

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.LOCATION_SERVICE) // fallback check
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

        if (sm == null) {
            compassState = CompassState(
                isAvailable = false,
                errorMessage = "Sensor Manager tidak tersedia pada perangkat ini."
            )
            return@DisposableEffect onDispose {}
        }

        val rotationVectorSensor = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val accelerometerSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometerSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (rotationVectorSensor == null && (accelerometerSensor == null || magnetometerSensor == null)) {
            compassState = CompassState(
                isAvailable = false,
                errorMessage = "Sensor kompas (Magnetometer) tidak ditemukan pada perangkat ini."
            )
            return@DisposableEffect onDispose {}
        }

        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)
        val lastAccelerometer = FloatArray(3)
        val lastMagnetometer = FloatArray(3)
        var lastAccelerometerSet = false
        var lastMagnetometerSet = false

        // Low-pass filter smoothed sin/cos components to avoid 360-degree boundary glitch
        var smoothedSin = 0.0
        var smoothedCos = 1.0
        val alpha = 0.15 // Smoothing factor

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return

                var rawAzimuthDeg: Double? = null

                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    val azimuthRad = orientation[0].toDouble()
                    rawAzimuthDeg = (Math.toDegrees(azimuthRad) + 360.0) % 360.0
                } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.size)
                    lastAccelerometerSet = true
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.size)
                    lastMagnetometerSet = true
                }

                if (rawAzimuthDeg == null && lastAccelerometerSet && lastMagnetometerSet) {
                    val r = FloatArray(9)
                    val i = FloatArray(9)
                    if (SensorManager.getRotationMatrix(r, i, lastAccelerometer, lastMagnetometer)) {
                        SensorManager.getOrientation(r, orientation)
                        val azimuthRad = orientation[0].toDouble()
                        rawAzimuthDeg = (Math.toDegrees(azimuthRad) + 360.0) % 360.0
                    }
                }

                if (rawAzimuthDeg != null) {
                    val currentRad = Math.toRadians(rawAzimuthDeg)
                    smoothedSin = smoothedSin * (1.0 - alpha) + sin(currentRad) * alpha
                    smoothedCos = smoothedCos * (1.0 - alpha) + cos(currentRad) * alpha

                    val smoothedHeading = (Math.toDegrees(atan2(smoothedSin, smoothedCos)) + 360.0) % 360.0

                    val accuracy = when (event.accuracy) {
                        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> CompassAccuracy.HIGH
                        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> CompassAccuracy.MEDIUM
                        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> CompassAccuracy.LOW
                        SensorManager.SENSOR_STATUS_UNRELIABLE -> CompassAccuracy.UNRELIABLE
                        else -> CompassAccuracy.MEDIUM
                    }

                    compassState = CompassState(
                        heading = smoothedHeading.toFloat(),
                        accuracy = accuracy,
                        isAvailable = true,
                        errorMessage = null
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                val acc = when (accuracy) {
                    SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> CompassAccuracy.HIGH
                    SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> CompassAccuracy.MEDIUM
                    SensorManager.SENSOR_STATUS_ACCURACY_LOW -> CompassAccuracy.LOW
                    SensorManager.SENSOR_STATUS_UNRELIABLE -> CompassAccuracy.UNRELIABLE
                    else -> compassState.accuracy
                }
                compassState = compassState.copy(accuracy = acc)
            }
        }

        if (rotationVectorSensor != null) {
            sm.registerListener(listener, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME)
        } else {
            accelerometerSensor?.let { sm.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME) }
            magnetometerSensor?.let { sm.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME) }
        }

        onDispose {
            sm.unregisterListener(listener)
        }
    }

    return compassState
}
