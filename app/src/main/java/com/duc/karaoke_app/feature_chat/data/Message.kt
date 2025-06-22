package com.duc.karaoke_app.feature_chat.data
import android.os.Parcelable
import com.duc.karaoke_app.feature_home.data.UserComment
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject


data class Message(
    @SerializedName("message_id")
    val messageId: Int,
    @SerializedName("room_id")
    val roomId: Int,
    @SerializedName("sender_id")
    val senderId: Int,
    val content: String,
    val type: String?,
    val createdAt: String,
    val status: String? = "sent",
    val sender: UserInfo?
){
    companion object {
        fun fromJson(json: JSONObject): Message {
            val senderJson = json.optJSONObject("sender")
            val sender = senderJson?.let {
                UserInfo(
                    userId = it.optInt("user_id"),
                    username = it.optString("username"),
                    avatarUrl = it.optString("avatar_url")
                )
            }
            return Message(
                messageId = json.getInt("message_id"),
                roomId    = json.getInt("room_id"),
                senderId  = json.getInt("sender_id"),
                content    = json.getString("content"),
                type       = null,
                createdAt  = json.getString("createdAt"),
                status = json.optString("status", "sent"),
                sender     = sender
            )
        }
    }
}

data class RoomResponse(
    @SerializedName("room_id") val roomId: Int,
    @SerializedName("created_by") val createdBy: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    var members: List<Member>,
    var messages: List<Message>,
)

data class Member(
    val id: Int,
    @SerializedName("room_id") val roomId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("joined_at") val joinedAt: String,
    val user: UserInfo
)
@Parcelize
data class UserInfo(
    @SerializedName("user_id") var userId: Int,
    val username: String,
    @SerializedName("avatar_url") val avatarUrl: String?
): Parcelable

data class UnreadRoomsResponse(
    val unreadRoomCount: Int
)


data class CommentLiveStreamList(
    @SerializedName("live_comment_id") val liveCommentId: Int,
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("comment_text") val commentText: String,
    @SerializedName("url_sticker") val urlSticker: String?,
    @SerializedName("url_image") val urlImage: String?,
    val status: String,
    @SerializedName("created_at") val commentTime: String,
    val userCommentLive: UserComment
) {
    companion object {
        fun fromJson(json: JSONObject): CommentLiveStreamList {
            val userJson = json.optJSONObject("userCommentLive")
            val user = UserComment(
                user_id = userJson?.optInt("user_id") ?: 0,
                username = userJson?.optString("username") ?: "Ẩn danh",
                avatar_url = userJson?.optString("avatar_url") ?: ""
            )
            return CommentLiveStreamList(
                liveCommentId = json.optInt("live_comment_id"),
                streamId = json.optInt("stream_id"),
                userId = json.optInt("user_id"),
                commentText = json.optString("comment_text"),
                urlSticker = json.optString("url_sticker").takeIf { it != "null" && it.isNotBlank() },
                urlImage = json.optString("url_image").takeIf { it != "null" && it.isNotBlank() },
                status = json.optString("status"),
                commentTime = json.optString("created_at"),
                userCommentLive = user
            )
        }
    }
}
