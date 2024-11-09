package com.duc.karaoke_app.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.duc.karaoke_app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GoogleSignInHelper {
    @SuppressLint("StaticFieldLeak")
    private var googleSignInClient: GoogleSignInClient?=null

    fun initialize(context: Context){
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(context, googleSignInOptions)
    }

    fun getClient(): GoogleSignInClient {
        return googleSignInClient ?: throw IllegalStateException("GoogleSignInHelper not initialized")
    }

    fun signOut(context:Context){
        googleSignInClient?.signOut()?.addOnCompleteListener{
            Log.w("SignOut","SignOut")
            val sharedPref = context.getSharedPreferences("MyAppPrefs",Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                clear()
                apply()
            }
        }
    }
}