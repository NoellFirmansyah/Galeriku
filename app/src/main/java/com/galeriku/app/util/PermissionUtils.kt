package com.galeriku.app.util

import android.Manifest
import android.os.Build

/**
 * Android 9-12L (API 28-32) pakai izin penyimpanan lama (READ_EXTERNAL_STORAGE).
 * Android 13+ (API 33+) pakai izin media granular (READ_MEDIA_IMAGES).
 */
fun requiredMediaPermission(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}
