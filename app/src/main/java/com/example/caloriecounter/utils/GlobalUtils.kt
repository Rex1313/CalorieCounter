package com.example.caloriecounter.utils

import android.content.res.Resources

const val VIDEO_WIDTH=876
const val VIDEO_HEIGHT=1080
const val VIDEO_URL="android.resource://com.example.caloriecounter/raw/berry2"

fun getScreenWidth(): Float {
    return Resources.getSystem().displayMetrics.widthPixels.toFloat()
}

fun getScreenHeight(): Float {
    return Resources.getSystem().displayMetrics.heightPixels.toFloat()
}