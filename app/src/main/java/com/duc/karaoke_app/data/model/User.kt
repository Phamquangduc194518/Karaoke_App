package com.duc.karaoke_app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class User(
    @SerializedName("user_id")
    val user_id: Int,
    val username: String,
    val email: String,
    val slogan: String?,
    val password: String,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    val phone: String? = null,
    val date_of_birth: String? = null,
    val gender: String? = null,
    val role: String? = null
) : Parcelable


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
    val user: UserComment
)

data class UserProfile(
    val username: String,
    val password: String,
    val slogan: String?,
    val phone: String? = null,
    @SerializedName("date_of_birth")
    val dateOfBirth: String? = null,
    val gender: String? = null

)

data class UserResponse(
    @SerializedName("userInfo") val userInfo: User
)

data class Comment(
    @SerializedName("song_id")
    val song_id: Int,
    @SerializedName("comment_text")
    val comment_text: String,
    @SerializedName("url_sticker")
    val urlSticker: String?,
    @SerializedName("url_image")
    val urlImage: String?
)

data class CommentDone(
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("song_id")
    val song_id: Int,
    @SerializedName("comment_text")
    val comment_text: String,
    @SerializedName("url_sticker")
    val urlSticker: String?,
    @SerializedName("url_image")
    val urlImage: String?,
    @SerializedName("comment_time")
    val comment_time: String,
    @SerializedName("user")
    val user: UserComment
)

data class UserComment(
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar_url")
    val avatar_url: String
)

data class Follow(
    @SerializedName("following_id")
    val followingId: Int,
    @SerializedName("follower_id")
    val followerId: Int
)

data class Following(
    @SerializedName("following_id")
    val followingId: Int
)

data class Follower(
    @SerializedName("follower_id")
    val followerId: Int
)

data class FollowResponse(
    val message: String?,
    val error: String?
)

data class FollowStatusResponse(
    val following: Boolean
)

data class FollowersResponse(
    val followers: List<FollowerItem>,
    val followerCount: Int
)

data class FollowerItem(
    @SerializedName("following_id")
    val followingId: Int,
    val follower: User
)

data class FollowingResponse(
    val following: List<FollowingItem>,
    val followingCount: Int
)

data class FollowingItem(
    @SerializedName("follower_id")
    val followerId: Int,
    val following: User
)

data class CommentVideo(
    @SerializedName("video_id")
    val videoId: Int,
    @SerializedName("comment_text")
    val commentText: String
)

data class CommentVideoDone(
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("song_id")
    val song_id: Int,
    @SerializedName("comment_text")
    val comment_text: String,
    @SerializedName("comment_time")
    val comment_time: String,
    @SerializedName("user")
    val user: UserComment
)

data class UploadAvatarResponse(
    val message: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)

data class Sticker(
    val id: Int,
    @SerializedName("sticker_url")
    val stickerUrl: String,
    val title: String,
    val category: String,
    val createdAt: String,
    val updatedAt: String
)

data class SearchResponse(
    val users: List<UserResult>?,
    val songs: List<SongResult>?
)

data class UserResult(
    val id: Int,
    val username: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    val slogan: String?,
    val email: String?
)

typealias SongResult = Songs

data class NotificationResponse(
    @SerializedName("notificationUser")
    val notificationUser: List<NotificationUser>
)

data class NotificationUser(
    val id: Int,
    @SerializedName("recipient_id")
    val recipientId: Int,
    @SerializedName("sender_id")
    val senderId: Int,
    val type: String,
    val message: String,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    val user: UserNotification
)

data class UserNotification(
    @SerializedName("user_id")
    val userId: Int,
    val username: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)





