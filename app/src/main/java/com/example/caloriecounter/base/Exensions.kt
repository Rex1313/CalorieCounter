package com.example.caloriecounter.base

import android.app.Activity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

