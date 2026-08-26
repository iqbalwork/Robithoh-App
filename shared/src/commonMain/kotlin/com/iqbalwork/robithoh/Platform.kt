package com.iqbalwork.robithoh

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform