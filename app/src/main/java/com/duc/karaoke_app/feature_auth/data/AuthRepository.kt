package com.duc.karaoke_app.feature_auth.data

import com.duc.karaoke_app.core.network.ApiService
import com.duc.karaoke_app.feature_home.data.ApiResponse
import com.duc.karaoke_app.feature_home.data.ForgotPasswordRequest
import com.duc.karaoke_app.feature_home.data.ForgotPasswordResponse
import com.duc.karaoke_app.feature_home.data.LoginRequest
import com.duc.karaoke_app.feature_home.data.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository {
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

    suspend fun forgotPassword(request: ForgotPasswordRequest): Response<ForgotPasswordResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.forgotPassword(request)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}