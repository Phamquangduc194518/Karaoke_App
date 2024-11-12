package com.duc.karaoke_app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import com.duc.karaoke_app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GoogleSignInHelper {
    @SuppressLint("StaticFieldLeak")
    private lateinit var googleSignInClient: GoogleSignInClient

    fun initialize(context: Context): GoogleSignInOptions{
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return googleSignInOptions
    }

    fun googleSignInInstance(context: Context):GoogleSignInClient{
        initialize(context)
        googleSignInClient = GoogleSignIn.getClient(context, initialize(context))
        return googleSignInClient
    }

    fun getClient(): GoogleSignInClient {
        return googleSignInClient ?: throw IllegalStateException("GoogleSignInHelper not initialized")
    }

    fun signInWithGoogle(): Intent? {
        val signInIntent = googleSignInClient?.signInIntent
        return signInIntent
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