package com.duc.karaoke_app.core.utils

import android.Manifest
import android.content.Context
import android.os.Build

object PermissionUtils {
    private val PREF_NAME = "karaoke_pref"

    fun setPermissionGranted(context: Context, permission: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean("${permission}_granted", true).apply()
    }

    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean("${permission}_granted", false)
    }

    fun areAllRequiredPermissionsGranted(context: Context): Boolean {
        val mic = isPermissionGranted(context, Manifest.permission.RECORD_AUDIO)
        val cam = isPermissionGranted(context, Manifest.permission.CAMERA)
        val notif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isPermissionGranted(context, Manifest.permission.POST_NOTIFICATIONS)
        } else true
        return mic && cam && notif
    }
}