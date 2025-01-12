package com.duc.karaoke_app.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File


object  GoogleSignInHelper{
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var appContext: Context

    fun init(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
        appContext = context.applicationContext

    }

    fun getGoogleSignInClient(): GoogleSignInClient {
        return googleSignInClient
    }

    fun getSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(appContext)
    }

    fun signOut(onSignOutComplete: (() -> Unit)? = null) {
        googleSignInClient.signOut().addOnCompleteListener {
            Log.d("GoogleSignInHelper", "Đã đăng xuất khỏi Google")
            onSignOutComplete?.invoke()
        }
    }

    fun getGoogleDriveService(account: GoogleSignInAccount?): Drive? {
        if (account == null) {
            Log.e("GoogleDrive", "Chưa đăng nhập google")
            return null
        }
        Log.e("GoogleDrive", "Tạo Google Drive service cho tài khoản: ${account.email}")
        val credential = GoogleAccountCredential.usingOAuth2(
            appContext, listOf(DriveScopes.DRIVE_FILE)
        ).setSelectedAccount(account.account)

        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory(),
            credential
        ).setApplicationName("Karaoke App").build()
    }


}