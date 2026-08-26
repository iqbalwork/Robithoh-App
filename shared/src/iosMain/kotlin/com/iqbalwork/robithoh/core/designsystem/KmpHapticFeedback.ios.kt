package com.iqbalwork.robithoh.core.designsystem

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType

class IosHapticFeedback : KmpHapticFeedback {
    private val lightImpact = UIImpactFeedbackGenerator(style = UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
    private val heavyImpact = UIImpactFeedbackGenerator(style = UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy)
    private val notificationFeedback = UINotificationFeedbackGenerator()

    override fun performClick() {
        lightImpact.prepare()
        lightImpact.impactOccurred()
    }

    override fun performSuccess() {
        notificationFeedback.prepare()
        notificationFeedback.notificationOccurred(UINotificationFeedbackType.UINotificationFeedbackTypeSuccess)
    }

    override fun performMilestone() {
        heavyImpact.prepare()
        heavyImpact.impactOccurred()
    }
}

actual fun getHapticFeedback(): KmpHapticFeedback = IosHapticFeedback()
