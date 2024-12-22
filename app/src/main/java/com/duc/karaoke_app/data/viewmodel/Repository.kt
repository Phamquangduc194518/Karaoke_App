package com.duc.karaoke_app.data.viewmodel

import com.duc.karaoke_app.data.model.ApiResponse
import com.duc.karaoke_app.data.model.LoginRequest
import com.duc.karaoke_app.data.model.RegisterRequest
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserProfile
import com.duc.karaoke_app.data.model.UserResponse
import com.duc.karaoke_app.data.model.YouTubeVideoItem
import com.duc.karaoke_app.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository() {
    private val apiService = ApiService.youtubeApi
    private val apiServiceToLogin = ApiService.loginApi


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
}