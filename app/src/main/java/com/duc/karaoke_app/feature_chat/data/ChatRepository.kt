package com.duc.karaoke_app.feature_chat.data

import com.duc.karaoke_app.feature_home.data.User
import com.duc.karaoke_app.core.network.ApiService
import com.duc.karaoke_app.feature_chat.data.remote.SocketManager
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

    suspend fun markAsDelivered(token:String){
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.markAsDelivered(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun markAsRead(token:String){
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.markAsRead(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

}