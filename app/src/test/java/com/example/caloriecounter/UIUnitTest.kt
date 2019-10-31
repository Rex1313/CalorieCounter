package com.example.caloriecounter

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UIUnitTest {
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
    }
    @Test
    fun addFavourite(){

    }
}