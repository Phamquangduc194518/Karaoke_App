package com.duc.karaoke_app.data.network

import android.content.Context
import android.util.Log
import com.duc.karaoke_app.R
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Collections

object DriveUploader {

    private var driveService: Drive? = null

    /**
     * Khởi tạo Google Drive Service
     */
    fun initDriveService(context: Context) {
        try {
            // Tải JSON từ res/raw
            val inputStream = context.resources.openRawResource(R.raw.drive) // Đổi thành file JSON đúng của bạn

            val credential = GoogleCredential.fromStream(inputStream)
                .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE))

            driveService = Drive.Builder(
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
            ).setApplicationName("YourAppName") // Đổi thành tên ứng dụng của bạn
                .build()

            Log.d("DriveUploader", "Google Drive service initialized successfully")

        } catch (e: IOException) {
            Log.e("DriveUploader", "Error initializing Google Drive service", e)
        }
    }

    /**
     * Upload file lên Google Drive
     */
    suspend fun uploadFile(localFile: java.io.File, folderId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val fileMetadata = File().apply {
                    name = localFile.name
                    parents = Collections.singletonList(folderId)
                }

                val mediaContent = FileContent("audio/mpeg", localFile) // Thay thế loại MIME của file nếu cần

                val file = driveService?.files()?.create(fileMetadata, mediaContent)
                    ?.setFields("id")
                    ?.execute()

                Log.d("DriveUploader", "File uploaded successfully: ${file?.id}")
                file?.id
            } catch (e: IOException) {
                Log.e("DriveUploader", "Error uploading file to Google Drive", e)
                null
            }
        }
    }
}
