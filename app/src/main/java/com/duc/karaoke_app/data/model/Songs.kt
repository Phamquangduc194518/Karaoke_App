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
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("song_name")
    val songName: String,

    @SerializedName("recording_path")
    val recordingPath: String,

    @SerializedName("upload_time")
    val uploadTime: Date = Date(),

    @SerializedName("likes_count")
    val likesCount: Int = 0,

    @SerializedName("comments_count")
    val commentsCount: Int = 0,

    @SerializedName("status")
    val status: String = "public" // default to "public"
)