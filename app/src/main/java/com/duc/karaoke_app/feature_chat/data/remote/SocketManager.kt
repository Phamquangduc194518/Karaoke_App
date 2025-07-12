package com.duc.karaoke_app.feature_chat.data.remote

import android.util.Log
import com.duc.karaoke_app.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject


object SocketManager {
    private const val DEFAULT_URL = BuildConfig.BASE_URL_LOGIN
//    private const val DEFAULT_URL = "http://192.168.1.7:8080"
    private var socketUrl: String = DEFAULT_URL
    private var token: String = ""
    private var socket: Socket? = null

    private var onConnectCallback: (() -> Unit)? = null
    private var onErrorCallback: ((Array<Any>) -> Unit)? = null
    fun init(authToken: String, url: String) {
        token = authToken
        socketUrl = url
    }

    fun connect() {
        Log.d("SocketManager", "🔥 connect() được gọi")
        if (socket?.connected() == true) return
        val opts = IO.Options().apply {
            query = "token=$token"
            reconnection = true
        }
        socket = IO.socket("${socketUrl}chat", opts).apply {
            on(Socket.EVENT_CONNECT) {
                Log.d("SocketManager", "Connecting to: ${socketUrl}chat?token=$token")
                onConnectCallback?.invoke()
            }
            on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("SocketManager", "Connect error: ${args.joinToString()}")
                onErrorCallback?.invoke(args)
            }

            on(Socket.EVENT_DISCONNECT) { args ->
                val reason = args.firstOrNull()?.toString() ?: "unknown"
                Log.w("SocketManager", "Disconnected: $reason")
            }
            on("private_message") { args ->
                val json = args.firstOrNull() as? JSONObject ?: return@on
            }
            on("message_status_updated"){args ->
                val json = args.firstOrNull() as? JSONObject ?: return@on
            }
            connect()
        }

    }

    fun sendMessage(to: Int, content: String) {
        val data = JSONObject().apply {
            put("content", content)
            put("to", to)
        }
        val message = JSONObject()
        message.put("event", "private_message")
        message.put("data", data)


        socket?.emit("message", message)
        Log.e("Data send Socket", message.toString())
    }

    fun markAsDelivered(messageId: Int, userId: Int) {
        val data = JSONObject().apply {
            put("messageId", messageId)
            put("userId", userId)
        }
        val message = JSONObject().apply {
            put("event", "mark_delivered")
            put("data", data)
        }
        socket?.emit("message", message)
        Log.d("SocketManager", "Marked as delivered: $data")
    }

    fun markAsRead(messageId: Int,  userId: Int) {
        val data = JSONObject().apply {
            put("messageId", messageId)
            put("userId", userId)
        }
        val message = JSONObject().apply {
            put("event", "mark_read")
            put("data", data)
        }
        socket?.emit("message", message)
        Log.d("SocketManager", "Marked as Read: $data")
    }

    fun onMessageReceived(listener: (message: JSONObject) -> Unit) {
        socket?.on("private_message") { args ->
            (args.firstOrNull() as? JSONObject)?.let{ json ->
                Log.e("SocketManager", "Received private_message: $json")
                listener(json)
            }
        }
    }

    fun onDisconnect(listener: () -> Unit) {
        socket?.on(Socket.EVENT_DISCONNECT) { listener() }
    }

    fun disconnect() {
        socket?.let {
            if (it.connected()) it.disconnect()
        }
        socket = null
    }

    fun onUserConnected(listener: (userId: Int) -> Unit) {
        socket?.on("user_connected") { args ->
            (args.firstOrNull() as? JSONObject)?.let { json ->
                val uid = json.optInt("userId")
                listener(uid)
            }
        }
    }
}
