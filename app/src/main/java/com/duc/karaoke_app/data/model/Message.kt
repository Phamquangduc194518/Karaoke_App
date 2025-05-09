package com.duc.karaoke_app.data.model
import android.os.Parcelable
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