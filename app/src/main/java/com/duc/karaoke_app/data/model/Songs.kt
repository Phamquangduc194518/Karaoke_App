package com.duc.karaoke_app.data.model

import com.google.gson.annotations.SerializedName

data class Songs(
    val id: Int,
    val title: String,
    val subTitle: String,
    val artist: String,
    val genre: String,
    val lyrics: String,
    @SerializedName("audio_url")
    val audioUrl: String,
    @SerializedName("cover_image_url")
    val coverImageUrl: String
)
data class Album(
    val title: String,
    val artist: String,
    val coverUrl: String // URL ảnh bìa
)
