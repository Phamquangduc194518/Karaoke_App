package com.duc.karaoke_app.data.Repository

import com.duc.karaoke_app.data.model.ApiResponse
import com.duc.karaoke_app.data.model.Message
import com.duc.karaoke_app.data.model.RegisterRequest
import com.duc.karaoke_app.data.model.RoomResponse
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserInfo
import com.duc.karaoke_app.data.network.ApiService
import com.duc.karaoke_app.utils.SocketManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ChatRepository() {
    private val apiServiceToLogin = ApiService.loginApi

    suspend fun getOnlineFollowingUsers(token:String): Response<List<UserInfo>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getOnlineFollowingUsers(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getProfile(token: String): Response<User> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getProfile(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getRooms(token:String): Response<List<RoomResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getRooms(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getMessages(token:String, roomId: Int): Response<List<Message>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getMessages(token, roomId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun sendMessage(content: String, toUserId: Int){
        SocketManager.sendMessage(toUserId, content)
    }
}