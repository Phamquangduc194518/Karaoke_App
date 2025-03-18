package com.duc.karaoke_app.data.viewmodel

import android.content.Context
import android.util.Log
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.AlbumDetailList
import com.duc.karaoke_app.data.model.Albums
import com.duc.karaoke_app.data.model.ApiResponse
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.CommentVideo
import com.duc.karaoke_app.data.model.CommentVideoDone
import com.duc.karaoke_app.data.model.Favorite
import com.duc.karaoke_app.data.model.FavoriteListResponse
import com.duc.karaoke_app.data.model.FavoritePost
import com.duc.karaoke_app.data.model.Follow
import com.duc.karaoke_app.data.model.FollowResponse
import com.duc.karaoke_app.data.model.FollowStatusResponse
import com.duc.karaoke_app.data.model.FollowersResponse
import com.duc.karaoke_app.data.model.Following
import com.duc.karaoke_app.data.model.FollowingResponse
import com.duc.karaoke_app.data.model.LiveStreamRequest
import com.duc.karaoke_app.data.model.LoginRequest
import com.duc.karaoke_app.data.model.Lyric
import com.duc.karaoke_app.data.model.NotificationResponse
import com.duc.karaoke_app.data.model.Post
import com.duc.karaoke_app.data.model.ReadNotificationResponse
import com.duc.karaoke_app.data.model.RecordedSongs
import com.duc.karaoke_app.data.model.RegisterRequest
import com.duc.karaoke_app.data.model.SearchResponse
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.Sticker
import com.duc.karaoke_app.data.model.Topic
import com.duc.karaoke_app.data.model.UploadAvatarResponse
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserProfile
import com.duc.karaoke_app.data.model.topSong
import com.duc.karaoke_app.data.network.ApiService
import com.google.api.services.drive.Drive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response

class Repository() {
    private val apiService = ApiService.youtubeApi
    private val apiServiceToLogin = ApiService.loginApi
    private var driveService: Drive? = null
    private val folderId = "1sek-KlPDD0HXqqsTy6phX3yunky_N6pl"


    suspend fun registerUser(request: RegisterRequest): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.createAccount(request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun loginUser(request: LoginRequest): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.login(request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun logOutUser(token: String): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.logout(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun updateUser(token: String, userProfile: UserProfile): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.updateUser(token, userProfile)
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

    suspend fun getSongList(token: String): Response<List<Songs>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getSongList(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getTopSong(token: String): Response<List<topSong>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getTopSong(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getItemSlide(): List<Int> {
        val listSlide = listOf(
            R.drawable.mau_poster_ca_nhac,
            R.drawable.mau_poster_ca_nhac_2,
            R.drawable.mau_poster_ca_nhac_3,
            R.drawable.mau_poster_ca_nhac_4,
            R.drawable.mau_poster_ca_nhac_5,
            R.drawable.mau_poster_ca_nhac_6,
        )
        return listSlide
    }

    suspend fun getProfileStar(token: String): Response<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getProfileStar(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getAllAlbum(): Response<List<Albums>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getAllAlbum()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun createRecordedSong(
        token: String,
        recorded: RecordedSongs
    ): Response<RecordedSongs> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.createRecordedSong(token, recorded)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getRecordedSongList(token: String): Response<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getRecordedSongList(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun createComment(token: String, comment: Comment): Response<Comment> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.createComment(token, comment)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getComments(songId: Int): Response<List<CommentDone>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getComments(songId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun createLiveStream(
        token: String,
        liveStream: LiveStreamRequest
    ): Response<LiveStreamRequest> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.createLiveStream(token, liveStream)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getDuetSong(): Response<List<Songs>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getDuetSong()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getDuetLyric(lyricName: String): Response<List<Lyric>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getDuetLyric(lyricName)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getAllTopicsWithVideo(): Response<List<Topic>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getAllTopicsWithVideo()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun createIsFavorite(token: String, songId: Int): Response<Favorite> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Favorite(songId = songId)
                apiServiceToLogin.createIsFavorite(token, request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun removeIsFavorite(token: String, songId: Int): Response<FollowResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.removeIsFavorite(token, songId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getIsFavorite(token: String): Response<FavoriteListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getIsFavorite(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getIsFavoriteToSongID(token: String): Response<List<Int>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getIsFavoriteToSongID(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getUserInfo(token: String, user_id: Int): Response<User> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getUserInfo(token, user_id)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun follow(token: String, followingId: Int): Response<FollowResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Following(followingId = followingId)
                apiServiceToLogin.follow(token, request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun unfollow(token: String, followingId: Int): Response<FollowResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Following(followingId = followingId)
                apiServiceToLogin.unfollow(token, request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun checkFollowStatus(token: String, followingId: Int): Response<FollowStatusResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.checkFollowStatus(token, followingId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getFollowers(token: String, userId: Int): Response<FollowersResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getFollowers(token, userId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getFollowing(token: String, userId: Int): Response<FollowingResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getFollowing(token, userId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getSongsByAlbum(albumId: Int): Response<AlbumDetailList> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getSongsByAlbum(albumId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun createCommentVideo(token: String, comment: CommentVideo): Response<CommentVideo> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.createCommentVideo(token, comment)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getCommentVideo(videoId: Int): Response<List<CommentVideoDone>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getCommentsVideo(videoId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun uploadAvatar(token: String, image: java.io.File): Response<UploadAvatarResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Tạo RequestBody cho file
                val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
                // Tạo MultipartBody.Part
                val body = MultipartBody.Part.createFormData("image", image.name, requestFile)
                apiServiceToLogin.uploadAvatar(token, body)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun uploadImagePost(image: java.io.File): Response<UploadAvatarResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Tạo RequestBody cho file
                val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
                // Tạo MultipartBody.Part
                val body = MultipartBody.Part.createFormData("image", image.name, requestFile)
                apiServiceToLogin.uploadImagePost(body)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getStickers(): Response<List<Sticker>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getStickers()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun search(query:String, type: String?): Response<SearchResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.search(query, type)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getFollowNotification(token: String): Response<NotificationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getFollowNotification(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun unreadNotifications(token: String): Response<NotificationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.unreadNotifications(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun readNotification(token: String, notificationId: Int): Response<ReadNotificationResponse>{
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.readNotification(token, notificationId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun createIsFavoritePost(token: String, postId: Int): Response<FavoritePost> {
        return withContext(Dispatchers.IO) {
            try {
                val request = FavoritePost(postId = postId)
                apiServiceToLogin.createIsFavoritePost(token, request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun removeIsFavoritePost(token: String, postId: Int): Response<FollowResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.removeIsFavoritePost(token, postId)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getIsFavoritePostToSongID(token: String): Response<List<Int>> {
        return withContext(Dispatchers.IO) {
            try {
                apiServiceToLogin.getIsFavoritePostToSongID(token)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}