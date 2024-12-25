package com.duc.karaoke_app.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?=null,
    val phone: String?=null,
    val date_of_birth: String?=null,
    val gender: String?=null
)


data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)
data class ApiResponse(
    val message: String,
    val token: String,
)
data class UserProfile(
    val username: String,
    val password: String,
    val phone: String?=null,
    @SerializedName("date_of_birth")
    val dateOfBirth: String?=null,
    val gender: String?=null

)

data class UserResponse(
    @SerializedName("userInfo") val userInfo: User
)