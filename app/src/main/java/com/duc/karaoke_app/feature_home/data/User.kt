package com.duc.karaoke_app.feature_home.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
    val role: String? = null,
    val rank: String? = null
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

data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val message: String
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
    val gender: String? = null,
    @SerializedName("avatar_url")
    val avatarUrl: String? =null
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

@Parcelize
data class LiveStreamStatus(
    val status: String
): Parcelable
@Parcelize
data class FollowingStar(
    @SerializedName("user_id")
    val userId: Int,
    val username: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val liveStream: LiveStreamStatus?
): Parcelable

data class AccountWithFollowers(
    @SerializedName("following_id")
    val followingId: Int,
    val followersCount: Int,
    val following: FollowingStar
)

data class FollowResponse(
    val message: String?,
    val error: String?
)

data class ReadNotificationResponse(
    val message: String?,
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
    val follower: Follower
)

data class FollowingResponse(
    val following: List<FollowingItem>,
    val followingCount: Int
)

data class FollowingItem(
    @SerializedName("follower_id")
    val followerId: Int,
    val following: Follower
)

data class Follower(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)

data class CommentVideo(
    @SerializedName("video_id")
    val videoId: Int,
    @SerializedName("comment_text")
    val commentText: String,
    @SerializedName("url_sticker")
    val urlSticker: String?,
    @SerializedName("url_image")
    val urlImage: String?
)

data class CommentVideoDone(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("video_id")
    val videoId: Int,
    @SerializedName("comment_text")
    val commentText: String,
    @SerializedName("url_sticker")
    val urlSticker: String?,
    @SerializedName("url_image")
    val urlImage: String?,
    @SerializedName("comment_time")
    val commentTime: String,
    @SerializedName("user")
    val user: UserComment
)

//data class UploadAvatarResponse(
//    val message: String,
//    @SerializedName("avatar_url")
//    val avatarUrl: String
//)

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
    var isRead: Boolean,
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

data class FavoritePost(
    @SerializedName("post_id")
    val postId: Int
)

data class VerifyPurchaseRequest(
    val user_id: String,
    val packageName: String,
    val productId: String,
    val purchaseToken: String
)

data class VerifyPurchaseResponse(
    val success : Boolean
)

data class CommentLiveStreamRequest(
    @SerializedName("stream_id")
    val streamId: String,
    @SerializedName("comment_text")
    val commentText: String,
    @SerializedName("url_sticker")
    val urlSticker: String?,
    @SerializedName("url_image")
    val urlImage: String?,
)
data class CommentResponse(
    val message: String,
    val comment: CommentLiveStream
)

data class CommentLiveStream(
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("live_comment_id")
    val liveCommentId: Int,
    @SerializedName("stream_id")
    val streamId: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("comment_text")
    val commentText: String,
    @SerializedName("url_sticker")
    val urlSticker: String?,
    @SerializedName("url_image")
    val urlImage: String?,
)

//data class CommentLiveStreamList(
//    @SerializedName("live_comment_id")
//    val liveCommentId: Int,
//    @SerializedName("stream_id")
//    val streamId: Int,
//    @SerializedName("user_id")
//    val userId: Int,
//    @SerializedName("comment_text")
//    val commentText: String,
//    @SerializedName("url_sticker")
//    val urlSticker: String?,
//    @SerializedName("url_image")
//    val urlImage: String?,
//    val status: String,
//    @SerializedName("created_at")
//    val commentTime: String,
//    val userCommentLive: UserComment
//)

data class DeviceTokenRequest(
    val deviceToken: String
)

data class SongRequest(
    val title: String,
    val content: String,
    val contactInformation: String
)

data class UpdateSongStatusRequest(
    val status: String
)

data class CheckPostingConditionResponse(
    val canPost: Boolean
)

data class ActivityStatisticsResponse(
    @SerializedName("coverPostCount")
    val coverPostCount: Int,
    @SerializedName("likeCoverCount")
    val likeCoverCount: Int,
    @SerializedName("likeSongCount")
    val likeSongCount: Int,
    @SerializedName("commentCount")
    val commentCount: Int
)








