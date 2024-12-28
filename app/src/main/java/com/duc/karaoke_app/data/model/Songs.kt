package com.duc.karaoke_app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
