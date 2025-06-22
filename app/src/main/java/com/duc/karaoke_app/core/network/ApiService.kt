package com.duc.karaoke_app.core.network

import com.duc.karaoke_app.BuildConfig
import com.duc.karaoke_app.feature_chat.data.CommentLiveStreamList
import com.duc.karaoke_app.feature_home.data.AccountWithFollowers
import com.duc.karaoke_app.feature_home.data.ActivityStatisticsResponse
import com.duc.karaoke_app.feature_home.data.AlbumDetailList
import com.duc.karaoke_app.feature_home.data.Albums
import com.duc.karaoke_app.feature_home.data.ApiResponse
import com.duc.karaoke_app.feature_home.data.CheckPostingConditionResponse
import com.duc.karaoke_app.feature_home.data.Comment
import com.duc.karaoke_app.feature_home.data.CommentDone
import com.duc.karaoke_app.feature_home.data.CommentLiveStreamRequest
import com.duc.karaoke_app.feature_home.data.CommentResponse
import com.duc.karaoke_app.feature_home.data.CommentVideo
import com.duc.karaoke_app.feature_home.data.CommentVideoDone
import com.duc.karaoke_app.feature_home.data.DeviceTokenRequest
import com.duc.karaoke_app.feature_home.data.Favorite
import com.duc.karaoke_app.feature_home.data.FavoriteListResponse
import com.duc.karaoke_app.feature_home.data.FavoritePost
import com.duc.karaoke_app.feature_home.data.FollowResponse
import com.duc.karaoke_app.feature_home.data.FollowStatusResponse
import com.duc.karaoke_app.feature_home.data.FollowersResponse
import com.duc.karaoke_app.feature_home.data.Following
import com.duc.karaoke_app.feature_home.data.FollowingResponse
import com.duc.karaoke_app.feature_home.data.ForgotPasswordRequest
import com.duc.karaoke_app.feature_home.data.ForgotPasswordResponse
import com.duc.karaoke_app.feature_home.data.LiveStream
import com.duc.karaoke_app.feature_home.data.LiveStreamRequest
import com.duc.karaoke_app.feature_home.data.LiveStreamResponse
import com.duc.karaoke_app.feature_home.data.LoginRequest
import com.duc.karaoke_app.feature_home.data.Lyric
import com.duc.karaoke_app.feature_chat.data.Message
import com.duc.karaoke_app.feature_home.data.NotificationResponse
import com.duc.karaoke_app.feature_home.data.Post
import com.duc.karaoke_app.feature_home.data.ReadNotificationResponse
import com.duc.karaoke_app.feature_home.data.RecommendationResponse
import com.duc.karaoke_app.feature_home.data.RecordedSong
import com.duc.karaoke_app.feature_home.data.RecordedSongs
import com.duc.karaoke_app.feature_home.data.RegisterRequest
import com.duc.karaoke_app.feature_chat.data.RoomResponse
import com.duc.karaoke_app.feature_chat.data.UnreadRoomsResponse
import com.duc.karaoke_app.feature_home.data.SearchResponse
import com.duc.karaoke_app.feature_home.data.SongRequest
import com.duc.karaoke_app.feature_home.data.Songs
import com.duc.karaoke_app.feature_home.data.Sticker
import com.duc.karaoke_app.feature_home.data.Topic
import com.duc.karaoke_app.feature_home.data.UpdateSongStatusRequest
import com.duc.karaoke_app.feature_home.data.User
import com.duc.karaoke_app.feature_chat.data.UserInfo
import com.duc.karaoke_app.feature_home.data.UserProfile
import com.duc.karaoke_app.feature_home.data.VerifyPurchaseRequest
import com.duc.karaoke_app.feature_home.data.VerifyPurchaseResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.duc.karaoke_app.feature_home.data.YouTubeResponse
import com.duc.karaoke_app.feature_home.data.topSong
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("search")
    suspend fun listKaraoke(
        @Query("part") part: String = "snippet",
        @Query("q") q: String = "karaoke",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int= 50,
        @Query("videoEmbeddable") videoEmbeddable: String = "true",
        @Query("key") apiKey: String
    ): Response<YouTubeResponse>

    @GET("search")
    suspend fun searchKaraokeVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int= 5,
        @Query("videoEmbeddable") videoEmbeddable: String = "true",
        @Query("key") apiKey: String
    ): Response<YouTubeResponse>

    @POST("/api/register")
    suspend fun createAccount(@Body request: RegisterRequest): Response<ApiResponse>

    @POST("/api/login")
    suspend fun login (@Body request: LoginRequest): Response<ApiResponse>

    @POST("api/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse>

    @PATCH("/api/updateProfile")
    suspend fun updateUser(@Header("Authorization") token: String, @Body profile: UserProfile): Response<ApiResponse>

    @GET("/api/userProfile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<User>

    @GET("/api/song/getSong")
    suspend fun getSongList(@Header("Authorization") token: String): Response<List<Songs>>

    @GET("/api/song/getTopSong")
    suspend fun getTopSong(@Header("Authorization") token: String): Response<List<topSong>>

    @GET("/api/getStarAccount")
    suspend fun getProfileStar(): Response<List<AccountWithFollowers>>

    @GET("/api/song/getAllAlbum")
    suspend fun getAllAlbum(): Response<List<Albums>>

    @POST("/api/createRecordedSong")
    suspend fun createRecordedSong(@Header("Authorization") token: String, @Body recorded: RecordedSongs): Response<RecordedSongs>

    @GET("/api/getRecordedSongList")
    suspend fun getRecordedSongList(@Header("Authorization") token: String): Response<List<Post>>

    @POST("/api/createComment")
    suspend fun createComment(@Header("Authorization") token: String, @Body comment: Comment) : Response<Comment>

    @GET("/api/getComments/{song_id}")
    suspend fun getComments(@Path("song_id") songId: Int) : Response<List<CommentDone>>

    @POST("/api/liveStream/createLiveStream")
    suspend fun createLiveStream(@Header("Authorization") token: String, @Body liveStream: LiveStreamRequest): Response<LiveStreamResponse>

    @PATCH("/api/liveStream/updateLiveStream")
    suspend fun updateLiveStream(@Header("Authorization") token: String): Response<ReadNotificationResponse>

    @GET("/api/liveStream/getLiveStreamList")
    suspend fun getLiveStreamList(): Response<LiveStream>

    @GET("/api/song/getSongDuet")
    suspend fun getDuetSong(): Response<List<Songs>>

    @GET("/api/song/getDuetLyric")
    suspend fun getDuetLyric(@Query("title") title: String): Response<List<Lyric>>

    @GET("/api/getAllTopicsWithVideo")
    suspend fun getAllTopicsWithVideo(): Response<List<Topic>>

    @POST("/api/createIsFavorite")
    suspend fun createIsFavorite(@Header("Authorization") token: String, @Body request: Favorite): Response<Favorite>

    @DELETE("/api/removeIsFavorite/{song_id}")
    suspend fun removeIsFavorite(@Header("Authorization") token: String, @Path("song_id") songId: Int): Response<FollowResponse>

    @GET("/api/getIsFavorite")
    suspend fun getIsFavorite(@Header("Authorization") token: String): Response<FavoriteListResponse>

    @GET("/api/getIsFavoriteToSongID")
    suspend fun getIsFavoriteToSongID(@Header("Authorization") token: String): Response<List<Int>>

    @GET("/api/getUserProfile/{user_id}")
    suspend fun getUserInfo(@Header("Authorization") token: String, @Path("user_id") user_id: Int): Response<User>

    @POST("/api/follow")
    suspend fun follow(@Header("Authorization") token: String, @Body request: Following): Response<FollowResponse>

    @POST("/api/unfollow")
    suspend fun unfollow(@Header("Authorization") token: String, @Body request: Following): Response<FollowResponse>

    @GET("/api/checkFollowStatus/{following_id}")
    suspend fun checkFollowStatus(@Header("Authorization") token: String, @Path("following_id") request: Int): Response<FollowStatusResponse>

    @GET("/api/followers/{user_id}")
    suspend fun getFollowers(@Header("Authorization") token: String, @Path("user_id") request: Int): Response<FollowersResponse>

    @GET("/api/following/{user_id}")
    suspend fun getFollowing(@Header("Authorization") token: String, @Path("user_id") request: Int): Response<FollowingResponse>

    @GET("/api/song/getSongsByAlbum/{album_id}")
    suspend fun getSongsByAlbum(@Path("album_id") albumId: Int): Response<AlbumDetailList>

    @POST("/api/CreateCommentVideo")
    suspend fun createCommentVideo(@Header("Authorization") token: String, @Body comment: CommentVideo) : Response<CommentVideo>

    @GET("/api/getCommentVideoList/{video_id}")
    suspend fun getCommentsVideo(@Path("video_id") videoId: Int) : Response<List<CommentVideoDone>>

//    @Multipart
//    @POST("/api/uploadAvatar")
//    suspend fun uploadAvatar(@Header("Authorization") token: String,  @Part image: MultipartBody.Part): Response<UploadAvatarResponse>

    @GET("/api/stickers")
    suspend fun getStickers() : Response<List<Sticker>>

    @GET("/api/search")
    suspend fun search(@Query("q") query: String, @Query("type") type: String?=null): Response<SearchResponse>

    @GET("/api/getFollowNotification")
    suspend fun getFollowNotification(@Header("Authorization") token: String): Response<NotificationResponse>

    @GET("/api/unreadNotifications")
    suspend fun unreadNotifications(@Header("Authorization") token: String): Response<NotificationResponse>

    @PATCH("/api/readNotifications/{notificationId}")
    suspend fun readNotification(@Header("Authorization") token: String, @Path("notificationId") notificationId: Int): Response<ReadNotificationResponse>

    @GET("/api/CheckPostingCondition")
    suspend fun CheckPostingCondition(@Header("Authorization") token: String): Response<CheckPostingConditionResponse>

    @POST("/api/createIsFavoritePost")
    suspend fun createIsFavoritePost(@Header("Authorization") token: String, @Body request: FavoritePost): Response<FavoritePost>

    @DELETE("/api/removeIsFavoritePost/{post_id}")
    suspend fun removeIsFavoritePost(@Header("Authorization") token: String, @Path("post_id") postId: Int): Response<FollowResponse>

    @GET("/api/getIsFavoritePostToSongID")
    suspend fun getIsFavoritePostToSongID(@Header("Authorization") token: String): Response<List<Int>>

    @POST("/api/verifyPurchase")
    suspend fun verifyPurchase(@Body request: VerifyPurchaseRequest): Response<VerifyPurchaseResponse>

    @POST("/api/liveStream/createCommentLiveStream")
    suspend fun createCommentLiveStream(@Header("Authorization") token: String, @Body request: CommentLiveStreamRequest): Response<CommentResponse>

    @GET("/api/liveStream/getCommentsByStream/{stream_id}")
    suspend fun getCommentsByStream(@Path("stream_id") liveStreamId: Int): Response<List<CommentLiveStreamList>>

    @POST("/api/updateDeviceToken")
    suspend fun updateDeviceToken(@Header("Authorization") token: String,@Body request: DeviceTokenRequest): Response<ReadNotificationResponse>

    @POST("/api/SongRequestFromUser")
    suspend fun songRequestFromUser(@Header("Authorization") token: String,@Body request: SongRequest): Response<ReadNotificationResponse>

    @POST("/api/forgotPassword")
    suspend fun forgotPassword (@Body request: ForgotPasswordRequest): Response<ForgotPasswordResponse>

    @GET("/api/getRecordedSongOfUser")
    suspend fun getRecordedSongOfUser (@Header("Authorization") token: String): Response<List<RecordedSong>>

    @POST("/api/makeSongPublic/{songPostId}")
    suspend fun makeSongPublic(@Path("songPostId")songPostId: Int, @Body request: UpdateSongStatusRequest):  Response<ReadNotificationResponse>

    @DELETE("/api/removeRecordedSong/{songPostId}")
    suspend fun removeRecordedSong(@Path("songPostId")songPostId: Int):  Response<ReadNotificationResponse>

    @GET("/api/getAllVideoOfTopic/{topicId}")
    suspend fun getAllVideoOfTopic(@Path("topicId")topicId: Int):  Response<Topic>

    @GET("/api/recommendSongs")
    suspend fun recommendSongs(@Header("Authorization") token: String): Response<RecommendationResponse>

    @GET("/api/activityStatistics")
    suspend fun activityStatistics(@Header("Authorization") token: String): Response<ActivityStatisticsResponse>

    @GET("/api/chat/getOnlineFollowingUsers")
    suspend fun getOnlineFollowingUsers(@Header("Authorization") token: String): Response<List<UserInfo>>

    @GET("/api/chat/rooms")
    suspend fun getRooms(@Header("Authorization") token: String): Response<List<RoomResponse>>

    @GET("/api/chat/rooms/{roomId}/messages")
    suspend fun getMessages(@Header("Authorization") token: String,@Path("roomId") roomId: Int): Response<List<Message>>

    @GET("/api/chat/pendingDelivery")
    suspend fun markAsDelivered(@Header("Authorization") token: String)

    @GET("/api/chat/pendingRead")
    suspend fun markAsRead(@Header("Authorization") token: String)

    @GET("/api/chat/unreadMessage")
    suspend fun unreadMessage(@Header("Authorization") token: String): Response<UnreadRoomsResponse>

    companion object RetrofitInstance{
        // Tạo Retrofit cho API YouTube
        // Tạo Interceptor để log request và response
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Log body đầy đủ
        }

        // Tạo OkHttpClient với Interceptor
        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        private const val BASE_URL_YOUTUBE = "https://www.googlzeapis.com/youtube/v3/"
        val youtubeApi: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL_YOUTUBE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

//        private const val BASE_URL_LOGIN= BuildConfig.BASE_URL_LOGIN
        private const val BASE_URL_LOGIN="http://192.168.1.5:8080/"
        val loginApi: ApiService by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL_LOGIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)  // Thêm OkHttpClient vào Retrofit
                .build()
                .create(ApiService::class.java)
        }
    }
}