package com.duc.karaoke_app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Songs(
    val id: Int,
    val title: String,
    val subTitle: String,
    @SerializedName("artist_id")
    val artist: Int,
    val genre: String,
    val lyrics: String,
    @SerializedName("audio_url")
    val audioUrl: String?,
    @SerializedName("url_image")
    val coverImageUrl: String?
) : Parcelable

data class Albums(
    val title: String,
    val subTitle: String,
    @SerializedName("artist_id")
    val artist: String,
    @SerializedName("cover_url")
    val coverUrl: String // URL ảnh bìa
)

data class RecordedSongs(

    @SerializedName("song_name")
    val songName: String,

    @SerializedName("recording_path")
    val recordingPath: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("status")
    val status: String = "public" // default to "public"
)

data class Post(
    @SerializedName("id")
    val post_id: Int,

    @SerializedName("song_name")
    val songName: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("recording_path")
    val recordingPath: String,

    @SerializedName("upload_time")
    val time: String,

    @SerializedName("likes_count")
    val likes_count: Int,

    @SerializedName("comments_count")
    val comments_count: Int,

    @SerializedName("status")
    val status: String = "public",

    @SerializedName("user")
    val user: UserPost
)
data class UserPost(
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar_url")
    val avatar_url: String
)
