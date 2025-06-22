package com.duc.karaoke_app.core.utils

import android.content.Context
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.util.HashMap

object CloudinaryManager {
    fun init(context: Context) {
        try {
            MediaManager.get()
        } catch (e: IllegalStateException) {
            val config: HashMap<String, String> = hashMapOf(
                "cloud_name" to "dwinhptbz",
                "api_key" to "275653597642644",
                "api_secret" to "iwmbTlKDNCYdI66UcSqq8AC-USQ",
                "secure" to "true"
            )
            MediaManager.init(context.applicationContext, config)
        }
    }
}