package com.electrosim

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.res.Resources
import org.mockito.Mockito

/**
 * Utility class for test-related common functionality
 */
object TestUtils {
    
    // Resource constants to use instead of R.drawable references
    object TestDrawables {
        const val VCC = 1001
        const val LED_OFF = 1002
        const val LED_ON = 1003
        const val AND_GATE = 1004
        const val OR_GATE = 1005
        const val XOR_GATE = 1006
        const val NOT_GATE = 1007
        const val NOR_GATE = 1008
    }
    
    /**
     * Sets up common resource mocking for components
     */
    fun setupResourceMocks(resources: Resources, bitmap: Bitmap) {
        // Mock all commonly used resources
        Mockito.`when`(BitmapFactory.decodeResource(resources, TestDrawables.VCC)).thenReturn(bitmap)
        Mockito.`when`(BitmapFactory.decodeResource(resources, TestDrawables.LED_OFF)).thenReturn(bitmap)
        Mockito.`when`(BitmapFactory.decodeResource(resources, TestDrawables.LED_ON)).thenReturn(bitmap)
        Mockito.`when`(BitmapFactory.decodeResource(resources, TestDrawables.AND_GATE)).thenReturn(bitmap)
        Mockito.`when`(BitmapFactory.decodeResource(resources, TestDrawables.OR_GATE)).thenReturn(bitmap)
        Mockito.`when`(BitmapFactory.decodeResource(resources, TestDrawables.XOR_GATE)).thenReturn(bitmap)
        Mockito.`when`(BitmapFactory.decodeResource(resources, TestDrawables.NOT_GATE)).thenReturn(bitmap)
        Mockito.`when`(BitmapFactory.decodeResource(resources, TestDrawables.NOR_GATE)).thenReturn(bitmap)
    }
} 