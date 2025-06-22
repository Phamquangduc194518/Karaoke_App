package com.duc.karaoke_app.feature_home.data

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

    @SerializedName("cover_image_url")
    val coverImageUrl: String,

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

    val statusFromAdmin: String,

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
    @SerializedName("stream_id")
    val streamId: Int,
    val title: String,
    val description: String?,
    @SerializedName("host_user_id")
    val hostUserId: Int,
    val status: String,
    val participantsCount: Int = 0,

)
data class LiveStreamRequest(
    val title: String
)

data class LiveStreamResponse(
    @SerializedName("host_user_id")
    val hostUserId :Int,
    val title: String,
    @SerializedName("stream_id")
    val streamId: Int,
    val status: String

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
    val subTitle: String,
    val duration: String,
    val type: String,
    val videos: List<Video>
)
@Parcelize
data class Video(
    @SerializedName("id")
    val videoId: Int,
    val topicId: Int,
    val title: String,
    val subTitle: String,
    val url: String,
    val thumbnail: String,
    val duration: String
): Parcelable

data class Favorite(
    @SerializedName("song_id")
    val songId: Int
)

@Parcelize
data class FavoriteSong(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("song_id")
    val songId: Int,
    @SerializedName("Song")
    val song : Songs
): Parcelable

data class FavoriteListResponse(
    val favoriteSongs: List<FavoriteSong>
)

data class AlbumDetailList(
    val title: String,
    val subTitle: String,
    @SerializedName("cover_url")
    val coverUrl: String,
    val artist: Artist,
    val songs: List<Songs>
)

data class RecordedSong(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("song_name")
    val songName: String,
    val title: String,
    @SerializedName("recording_path")
    val recordingPath: String,
    @SerializedName("cover_image_url")
    val coverImageUrl: String,
    @SerializedName("upload_time")
    val uploadTime: String,
    @SerializedName("likes_count")
    val likesCount: Int,
    @SerializedName("comments_count")
    val commentsCount: Int,
    val status: String,
    val statusFromAdmin: String
)

@Parcelize
data class RecommendationResponse(
    val recommendations: List<Songs>
): Parcelable

