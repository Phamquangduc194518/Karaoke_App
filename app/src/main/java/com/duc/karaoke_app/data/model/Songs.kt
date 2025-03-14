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
    val genre: String?=null,
    val lyrics: String,
    @SerializedName("audio_url")
    val audioUrl: String?,
    @SerializedName("url_image")
    val coverImageUrl: String?
) : Parcelable


data class topSong(
    @SerializedName("song_id")
    val songId: Int,
    val favoriteCount: Int,
    @SerializedName("Song")
    val song: Songs
)

@Parcelize
data class Albums(
    val id: Int,
    val title: String,
    val subTitle: String,
    @SerializedName("cover_url")
    val coverUrl: String, // URL ảnh bìa
    @SerializedName("albumArtist")
    val artist: Artist
): Parcelable

@Parcelize
data class Artist(
    val id: String,
    val name: String
): Parcelable

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

    @SerializedName("cover_image_url")
    val coverImageUrl: String,

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

data class LiveStream(
    val streamId: Int,
    val title: String,
    val description: String?,
    val hostUserId: Int,
    val status: Status,
    val participantsCount: Int = 0,
    val endedAt: Date?
) {
    enum class Status {
        ACTIVE, ENDED
    }
}

data class LiveStreamRequest(
    val title: String
)

data class Lyric(
    val start: Float,
    val end: Float,
    val text: String,
    val singer: String
)

data class Topic(
    val id: Int,
    val title: String,
    val videos: List<Video>
)
@Parcelize
data class Video(
    @SerializedName("id")
    val videoId: Int,
    val topicId: Int,
    val title: String,
    val url: String,
    val thumbnail: String
): Parcelable

data class Favorite(
    @SerializedName("song_id")
    val songId: Int
)

data class FavoriteSong(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("song_id")
    val songId: Int,
    @SerializedName("Song")
    val song : Songs
)
data class FavoriteListResponse(
    val favoriteSongs: List<FavoriteSong>
)

data class checkFavoriteListResponse(
    val favoriteSongIds: List<Int>
)

data class AlbumDetailList(
    val title: String,
    val subTitle: String,
    @SerializedName("cover_url")
    val coverUrl: String,
    val artist: Artist,
    val songs: List<Songs>
)