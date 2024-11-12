package com.duc.karaoke_app.data.viewmodel

import com.duc.karaoke_app.data.model.YouTubeVideoItem
import com.duc.karaoke_app.data.network.YouTubeApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KaraokeRepository() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val apiService = YouTubeApiService.RetrofitInstance.api

    fun firebaseAuthWithGoogle(idToken: String, onResult: (FirebaseUser?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(auth.currentUser)
                } else {
                    onResult(null)
                }
            }
    }

    suspend fun searchKaraoke(apiKey: String): Result<List<YouTubeVideoItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchKaraoke(apiKey = apiKey)
                if (response.isSuccessful) {
                    Result.success(response.body()!!.items)
                } else {
                    Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}