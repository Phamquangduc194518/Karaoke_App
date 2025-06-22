package com.duc.karaoke_app.feature_chat.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duc.karaoke_app.BuildConfig
import com.duc.karaoke_app.feature_chat.data.ChatRepository
import com.duc.karaoke_app.feature_chat.data.Message
import com.duc.karaoke_app.feature_chat.data.RoomResponse
import com.duc.karaoke_app.feature_home.data.User
import com.duc.karaoke_app.feature_chat.data.UserInfo
import com.duc.karaoke_app.feature_chat.presentation.ui.adapter.ChatHistoryAdapter
import com.duc.karaoke_app.feature_chat.presentation.ui.adapter.ChatRoomAdapter
import com.duc.karaoke_app.feature_chat.presentation.ui.adapter.OnlineUserAdapter
import com.duc.karaoke_app.core.utils.SingleLiveEvent
import com.duc.karaoke_app.feature_chat.data.remote.SocketManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelChat(val repository: ChatRepository, application: Application) :
    AndroidViewModel(application) {

    private val _chatRooms = MutableLiveData<List<RoomResponse>>()
    val chatRooms: LiveData<List<RoomResponse>> = _chatRooms

    private val _userProfile = SingleLiveEvent<User>()
    val userProfile: LiveData<User> get() = _userProfile

    private val _itemChatClick = MutableLiveData<Boolean>(false)
    val itemChatClick: LiveData<Boolean> get() = _itemChatClick

    private val _itemClickId = SingleLiveEvent<Int>()
    val itemClickId: LiveData<Int> get() = _itemClickId

    private val _chatHistory = MutableLiveData<List<Message>>()
    val chatHistory: LiveData<List<Message>> get() = _chatHistory

    private val _otherUser = MutableLiveData<UserInfo?>()
    val otherUser: LiveData<UserInfo?> get() = _otherUser

    // hiển thị thời gian khi click vào
    private val _messageClick = MutableLiveData<Boolean>(false)
    val messageClick: LiveData<Boolean> get() = _messageClick

    private val _userIdFromUserProfile = MutableLiveData<Int>(0)
    val userIdFromUserProfile: LiveData<Int> get() = _userIdFromUserProfile

    private var _toKenToMessenger = ""
    var content = MutableLiveData<String>("")
    val onlineUserAdapter = OnlineUserAdapter()
    val chatRoomAdapter = ChatRoomAdapter()
    val chatHistoryAdapter = ChatHistoryAdapter()

    init {
        saveTokenToMusicPlayerActivity()
//        SocketManager.init(_toKenToMessenger, BuildConfig.BASE_URL_LOGIN)
        SocketManager.init(_toKenToMessenger, "http://192.168.1.5:8080")
        SocketManager.connect()
        userProfile()
        getOnlineFollowingUsers()
        getRooms()

        chatHistoryAdapter.setOnClickItemView { message ->
            if (message != null) {
                _messageClick.value = true
            }
        }

        chatRoomAdapter.setOnItemChatClick { Room ->
            val other = Room.members.first {
                it.user.userId != userProfile.value?.user_id
            }
            _otherUser.value = other.user
            _itemClickId.value = Room.roomId
            _itemChatClick.value = true
        }

        SocketManager.onMessageReceived { json ->
            viewModelScope.launch(Dispatchers.Main) {
                Log.e("ViewModelChat", "Raw JSON received: $json")
                try {
                    val newMsg = Message.fromJson(json)
                    Log.e(
                        "ViewModelChat",
                        "Parsed Message: roomId=${newMsg.roomId}, content=${newMsg.content}, senderId=${newMsg.senderId}"
                    )
                    val currentUserId = userProfile.value?.user_id ?: 0
                    if (newMsg.senderId != currentUserId && newMsg.status == "sent") {
                        Log.d("ViewModelChat", "Marking as delivered: messageId=${newMsg.messageId}")
                        SocketManager.markAsDelivered(newMsg.messageId, currentUserId)
                        SocketManager.markAsRead(newMsg.messageId, currentUserId)
                    }
                    if (newMsg.roomId == _itemClickId.value) {
                        addNewMessage(newMsg)
                    } else {
                        Log.e(
                            "ViewModelChat",
                            "Message ignored: roomId=${newMsg.roomId} does not match current roomId=${_itemClickId.value}"
                        )
                    }
                    Log.e("socket.io", newMsg.content)
                } catch (e: Exception) {
                    Log.e("ViewModelChat", "Failed to parse JSON: ${e.message}")
                }
            }
        }
    }

    fun resetOnClickChatItem() {
        _itemChatClick.value = false
    }

    fun resetOnClickMessageItem() {
        _messageClick.value = false
    }

    fun setUserIdChat(userId: Int) {
        _userIdFromUserProfile.value = userId
        Log.d("ViewModelChat", "Set userId: ${userId}")
    }

    fun setOtherIdFromHistoryChat(other: UserInfo){
        _otherUser.value = other
        viewModelScope.launch {
            try {
                val response = repository.getRooms("Bearer $_toKenToMessenger")
                if (response.isSuccessful) {
                    val rooms = response.body() ?: listOf()
                    val room = rooms.find { room ->
                        room.members.any { it.user.userId == other.userId }
                    }
                    room?.let {
                        _itemClickId.value = it.roomId
                        getMessages()
                    }
                }
            } catch (e: Exception) {
                Log.e("setOtherIdFromHistoryChat", "Thất bại: ${e.message}")
            }
        }
    }

    private fun saveTokenToMusicPlayerActivity(): String? {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        if (token != null) {
            Log.e("Token To Messenger", "$token")
            _toKenToMessenger = token.trim()
        } else {
            Log.e("Token To Messenger", "Token không tồn tại")
        }
        return _toKenToMessenger
    }

    fun userProfile() {
        viewModelScope.launch {
            try {
                val response = repository.getProfile("Bearer $_toKenToMessenger")
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                } else {
                    Log.e("userProfileOfMessage", "Lỗi: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("userProfileOfMessage", "Thất bại: ${e.message}")
            }
        }
    }

    fun getOnlineFollowingUsers() {
        viewModelScope.launch {
            try {
                val response = repository.getOnlineFollowingUsers("Bearer $_toKenToMessenger")
                if (response.isSuccessful) {
                    onlineUserAdapter.updateOnlineUsers(response.body() ?: listOf())
                }
            } catch (e: Exception) {
                Log.e("getOnlineFollowingUsers", "Thất bại: ${e.message}")
            }
        }

    }


    fun getRooms() {
        viewModelScope.launch {
            try {
                val response = repository.getRooms("Bearer $_toKenToMessenger")
                if (response.isSuccessful) {
                    chatRoomAdapter.setCurrentUserId(userProfile.value?.user_id ?: 0)
                    chatRoomAdapter.updateRoom(response.body() ?: listOf())
                    repository.markAsDelivered("Bearer $_toKenToMessenger")
                }
            } catch (e: Exception) {
                Log.e("getRooms", "Thất bại: ${e.message}")
            }
        }
    }

    fun getMessages() {
        viewModelScope.launch {
            try {
                val response =
                    repository.getMessages("Bearer $_toKenToMessenger", _itemClickId.value ?: 0)
                if (response.isSuccessful) {
                    val messages = response.body() ?: emptyList()
                    _chatHistory.value = response.body()
                    chatHistoryAdapter.setCurrentUserId(userProfile.value?.user_id ?: 0)
                    chatHistoryAdapter.updateChatHistory(_chatHistory.value ?: listOf())
                    repository.markAsRead("Bearer $_toKenToMessenger")
                }
            } catch (e: Exception) {
                Log.e("getMessages", "Thất bại: ${e.message}")
            }
        }
    }

    fun sendMessage() {
        if (userIdFromUserProfile.value != 0) {
            val response =
                repository.sendMessage(content.value ?: "", userIdFromUserProfile.value ?: 0)
            val sentMsg = Message(
                messageId = 0,
                roomId = _itemClickId.value ?: 0,
                senderId = userProfile.value?.user_id ?: 0,
                content = content.value ?: "",
                type = null,
                createdAt = System.currentTimeMillis().toString(),
                sender = UserInfo(
                    userId = userProfile.value?.user_id ?: 0,
                    username = userProfile.value?.username ?: "",
                    avatarUrl = userProfile.value?.avatarUrl,
                )
            )
            content.value = ""
        } else {
            val response =
                repository.sendMessage(content.value ?: "", _otherUser.value?.userId ?: 0)
            val sentMsg = Message(
                messageId = 0,
                roomId = _itemClickId.value ?: 0,
                senderId = userProfile.value!!.user_id,
                content = content.value ?: "",
                type = null,
                createdAt = System.currentTimeMillis().toString(),
                sender = UserInfo(
                    userId = userProfile.value?.user_id ?: 0,
                    username = userProfile.value?.username ?: "",
                    avatarUrl = userProfile.value?.avatarUrl,
                )
            )
            content.value = ""
        }
    }

    fun addNewMessage(msg: Message) {
        val updatedList = _chatHistory.value.orEmpty().toMutableList()
        updatedList.add(msg)
        _chatHistory.postValue(updatedList)
        chatHistoryAdapter.addMessage(msg)
    }


}