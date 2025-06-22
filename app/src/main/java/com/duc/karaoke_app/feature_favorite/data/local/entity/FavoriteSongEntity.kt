package com.duc.karaoke_app.feature_favorite.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_songs")
data class FavoriteSongEntity (
    @PrimaryKey val songId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subTitle") val subTitle: String,
    @ColumnInfo(name = "artist") val artist: Int,
    @ColumnInfo(name = "lyrics") val lyrics: String,
    @ColumnInfo(name = "audioUrl") val audioUrl: String?,
    @ColumnInfo(name = "coverImageUrl") val coverImageUrl: String?,
    @ColumnInfo(name = "localAudioPath") val localAudioPath: String? = null
)