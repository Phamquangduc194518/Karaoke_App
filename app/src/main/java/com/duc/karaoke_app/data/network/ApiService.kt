package com.duc.karaoke_app.data.network

import com.duc.karaoke_app.data.model.Albums
import com.duc.karaoke_app.data.model.ApiResponse
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.LoginRequest
import com.duc.karaoke_app.data.model.Post
import com.duc.karaoke_app.data.model.RecordedSongs
import com.duc.karaoke_app.data.model.RegisterRequest
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserProfile
import com.duc.karaoke_app.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.duc.karaoke_app.data.model.YouTubeResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
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
    suspend fun logout(): Response<ApiResponse>

    @PATCH("/api/updateProfile")
    suspend fun updateUser(@Header("Authorization") token: String, @Body profile: UserProfile): Response<ApiResponse>

    @GET("/api/userProfile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserResponse>

    @GET("/api/song/getSong")
    suspend fun getSongList(@Header("Authorization") token: String): Response<List<Songs>>

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





    companion object RetrofitInstance{
        // Tạo Retrofit cho API YouTube
        private const val BASE_URL_YOUTUBE = "https://www.googleapis.com/youtube/v3/"
        val youtubeApi: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL_YOUTUBE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        // Tạo Retrofit cho API Localhost (đăng ký tài khoản)
        private const val BASE_URL_LOGIN="http://192.168.1.9:3000/"
        val loginApi: ApiService by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL_LOGIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}