package com.duc.karaoke_app.data.network

import com.duc.karaoke_app.data.model.AlbumDetailList
import com.duc.karaoke_app.data.model.Albums
import com.duc.karaoke_app.data.model.ApiResponse
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.CommentVideo
import com.duc.karaoke_app.data.model.CommentVideoDone
import com.duc.karaoke_app.data.model.Favorite
import com.duc.karaoke_app.data.model.FavoriteListResponse
import com.duc.karaoke_app.data.model.Follow
import com.duc.karaoke_app.data.model.FollowResponse
import com.duc.karaoke_app.data.model.FollowStatusResponse
import com.duc.karaoke_app.data.model.FollowersResponse
import com.duc.karaoke_app.data.model.Following
import com.duc.karaoke_app.data.model.FollowingResponse
import com.duc.karaoke_app.data.model.LiveStream
import com.duc.karaoke_app.data.model.LiveStreamRequest
import com.duc.karaoke_app.data.model.LoginRequest
import com.duc.karaoke_app.data.model.Lyric
import com.duc.karaoke_app.data.model.Post
import com.duc.karaoke_app.data.model.RecordedSongs
import com.duc.karaoke_app.data.model.RegisterRequest
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.Topic
import com.duc.karaoke_app.data.model.UploadAvatarResponse
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserProfile
import com.duc.karaoke_app.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.duc.karaoke_app.data.model.YouTubeResponse
import com.duc.karaoke_app.data.model.checkFavoriteListResponse
import com.duc.karaoke_app.data.model.topSong
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
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

//https://www.googleapis.com/youtube/v3/search?part=snippet&q=karaoke&type=video&maxResults=50&key=AIzaSyD7L2AC6LciuwBHR9qWt3QQI4qf1gH3Plg
//http://localhost:3000/api/register
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
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserResponse>

    @GET("/api/song/getSong")
    suspend fun getSongList(@Header("Authorization") token: String): Response<List<Songs>>

    @GET("/api/song/getTopSong")
    suspend fun getTopSong(@Header("Authorization") token: String): Response<List<topSong>>

    @GET("/api/admin/getAllAccount")
    suspend fun getProfileStar(@Header("Authorization") token: String): Response<List<User>>

    @GET("/api/song/getAllAlbum")
    suspend fun getAllAlbum(): Response<List<Albums>>

    @POST("/api/createRecordedSong")
    suspend fun createRecordedSong(@Header("Authorization") token: String, @Body recorded: RecordedSongs): Response<RecordedSongs>

    @GET("/api/getRecordedSongList")
    suspend fun getRecordedSongList(): Response<List<Post>>

    @POST("/api/createComment")
    suspend fun createComment(@Header("Authorization") token: String, @Body comment: Comment) : Response<Comment>

    @GET("/api/getComments/{song_id}")
    suspend fun getComments(@Path("song_id") songId: Int) : Response<List<CommentDone>>

    @POST("/api/liveStream/createLiveStream")
    suspend fun createLiveStream(@Header("Authorization") token: String, @Body liveStream: LiveStreamRequest): Response<LiveStreamRequest>

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

    @Multipart
    @POST("/api/uploadAvatar")
    suspend fun uploadAvatar(@Header("Authorization") token: String,  @Part image: MultipartBody.Part): Response<UploadAvatarResponse>

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

        // Tạo Retrofit cho API Localhost (đăng ký tài khoản)
        private const val BASE_URL_LOGIN="http://192.168.1.11:3000/"
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