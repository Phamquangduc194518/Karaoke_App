package com.duc.karaoke_app.data.viewmodel

import android.content.Context
import android.util.Log
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.Albums
import com.duc.karaoke_app.data.model.ApiResponse
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.LoginRequest
import com.duc.karaoke_app.data.model.Post
import com.duc.karaoke_app.data.model.RecordedSongs
import com.duc.karaoke_app.data.model.RegisterRequest
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserProfile
import com.duc.karaoke_app.data.model.UserResponse
import com.duc.karaoke_app.data.model.YouTubeVideoItem
import com.duc.karaoke_app.data.network.ApiService
import com.duc.karaoke_app.utils.GoogleSignInHelper
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository() {
    private val apiService = ApiService.youtubeApi
    private val apiServiceToLogin = ApiService.loginApi
    private var driveService: Drive? = null
    private val folderId = "1sek-KlPDD0HXqqsTy6phX3yunky_N6pl"


    suspend fun registerUser(request: RegisterRequest): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.createAccount(request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun loginUser(request: LoginRequest): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.login(request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun logOutUser(): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.logout()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun updateUser(token: String, userProfile: UserProfile): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.updateUser(token, userProfile)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getProfile(token: String): Response<UserResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getProfile(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getSongList(token: String): Response<List<Songs>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getSongList(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getItemSlide(): List<Int>{
        val listSlide = listOf(
            R.drawable.mau_poster_ca_nhac,
            R.drawable.mau_poster_ca_nhac_2,
            R.drawable.mau_poster_ca_nhac_3,
            R.drawable.mau_poster_ca_nhac_4,
            R.drawable.mau_poster_ca_nhac_5,
            R.drawable.mau_poster_ca_nhac_6,
        )
        return listSlide
    }

    suspend fun getProfileStar(token: String): Response<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getProfileStar(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getAllAlbum(): Response<List<Albums>>{
        return withContext(Dispatchers.IO){
            try {
                apiServiceToLogin.getAllAlbum()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun createRecordedSong(token: String, recorded: RecordedSongs): Response<RecordedSongs>{
        return withContext(Dispatchers.IO){
            try{
                apiServiceToLogin.createRecordedSong(token,recorded)
            }catch(e: Exception){
                throw e
            }
        }
    }

    suspend fun getRecordedSongList(): Response<List<Post>>{
        return withContext(Dispatchers.IO){
            try{
                apiServiceToLogin.getRecordedSongList()
            }catch(e: Exception){
                throw e
            }
        }
    }

    suspend fun createComment(token: String, comment: Comment): Response<Comment>{
        return withContext(Dispatchers.IO){
            try{
                apiServiceToLogin.createComment(token, comment)
            }catch(e: Exception){
                throw e
            }
        }
    }

    suspend fun getComments(songId: Int): Response<List<CommentDone>>{
        return withContext(Dispatchers.IO){
            try{
                apiServiceToLogin.getComments(songId)
            }catch(e: Exception){
                throw e
            }
        }
    }


}