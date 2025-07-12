package com.duc.karaoke_app.feature_chat.data.remote

import android.util.Log
import com.duc.karaoke_app.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object LiveStreamSocketManager {
    private const val BASE_URL = BuildConfig.BASE_URL_LOGIN
//    private const val BASE_URL = "http://192.168.1.5:8080"
    private var socket: Socket? = null
    private var streamId: Int = -1
    private var token: String = ""
    private var isListening = false

    private var commentListener: ((JSONObject) -> Unit)? = null

    fun init(streamId: Int, authToken: String) {
        this.streamId = streamId
        this.token = authToken
        Log.d("LiveSocket", "init() gọi với streamId=$streamId, token=$authToken")
    }

    fun connect() {
        if (socket != null && socket!!.connected()) {
            Log.d("LiveSocket", "Đã kết nối socket, bỏ qua connect()")
            return
        }

        Log.d("LiveSocket", "Gọi connect với query: streamId=$streamId, token=$token")

        val opts = IO.Options().apply {
            query = "streamId=$streamId&token=$token"
            reconnection = true
        }

        socket = IO.socket("${BASE_URL}livestream", opts).apply {
            on(Socket.EVENT_CONNECT) {
                Log.d("LiveSocket", "Kết nối thành công với livestream $streamId")
            }
            on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("LiveSocket", "Lỗi kết nối: ${args.joinToString()}")
            }
            on("new_comment") { args ->
                Log.d("LiveSocket", "Nhận comment mới từ socket")
                (args.firstOrNull() as? JSONObject)?.let { comment ->
                    commentListener?.invoke(comment)
                }
            }
            on(Socket.EVENT_DISCONNECT) {
                Log.w("LiveSocket", "Ngắt kết nối livestream $streamId")
            }
            connect()
        }
    }


    fun sendComment(commentJson: JSONObject) {
        socket?.emit("send_comment", commentJson)
    }

    fun listenNewComment(callback: (JSONObject) -> Unit) {
        if (!isListening) {
            commentListener = callback
            isListening = true
        }
    }

    fun disconnect() {
        socket?.off("new_comment")
        socket?.disconnect()
        socket = null
        isListening = false
    }
}
