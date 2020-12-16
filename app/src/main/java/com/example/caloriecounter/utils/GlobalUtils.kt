package com.example.caloriecounter.utils

import android.content.res.Resources
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import java.util.*

const val VIDEO_WIDTH = 876
const val VIDEO_HEIGHT = 1080
const val VIDEO_URL = "android.resource://com.example.caloriecounter/raw/berry2"

fun getScreenWidth(): Float {
    return Resources.getSystem().displayMetrics.widthPixels.toFloat()
}

fun getScreenHeight(): Float {
    return Resources.getSystem().displayMetrics.heightPixels.toFloat()
}

fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))


fun getRandomUUID(): String {
    return UUID.randomUUID().toString().replace("-", "").toUpperCase()
}