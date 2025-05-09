package com.duc.karaoke_app.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


object SocketManager {
    private const val DEFAULT_URL = "http://192.168.1.12:8080"
    private var socketUrl: String = DEFAULT_URL
    private var token: String = ""
    private var socket: Socket? = null

    private var onConnectCallback: (() -> Unit)? = null
    private var onErrorCallback: ((Array<Any>) -> Unit)? = null
    fun init(authToken: String, url: String = DEFAULT_URL) {
        token = authToken
        socketUrl = url
    }

    fun connect() {
        if (socket?.connected() == true) return
        val opts = IO.Options().apply {
            query = "token=$token"
            reconnection = true
        }
        socket = IO.socket(socketUrl, opts).apply {
            on(Socket.EVENT_CONNECT) {
                Log.d("SocketManager", "Socket connected!")
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
        socket?.emit("mark_delivered", data)
        Log.d("SocketManager", "Marked as delivered: $data")
    }

    fun markAsRead(messageId: Int) {
        val data = JSONObject().apply {
            put("message_id", messageId)
        }
        socket?.emit("message_read", data)
    }

    fun onMessageStatusUpdated( listener: (messageId: Int, status: String) -> Unit){
        socket?.on("message_status_updated"){args ->
            (args.firstOrNull() as? JSONObject)?.let {json ->
                val messageId = json.optInt("message_id")
                val status = json.optString("status")
                Log.d("SocketManager", "Status updated: messageId=$messageId, status=$status")
                listener(messageId, status)
            }
        }
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
