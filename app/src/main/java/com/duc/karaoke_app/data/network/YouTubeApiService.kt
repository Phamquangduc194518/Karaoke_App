package com.duc.karaoke_app.data.network

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.duc.karaoke_app.data.model.YouTubeResponse
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

//https://www.googleapis.com/youtube/v3/search?part=snippet&q=karaoke&type=video&maxResults=50&key=AIzaSyD7L2AC6LciuwBHR9qWt3QQI4qf1gH3Plg

interface YouTubeApiService {
    @GET("search")
    suspend fun searchKaraoke(
        @Query("part") part: String = "snippet",
        @Query("q") q: String = "karaoke",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int= 50,
        @Query("key") apiKey: String
    ): Response<YouTubeResponse>

    object RetrofitInstance{
        private const val BASE_URL="https://www.googleapis.com/youtube/v3/"
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        val api:YouTubeApiService by lazy{
            retrofit.create(YouTubeApiService::class.java)
        }
    }
}