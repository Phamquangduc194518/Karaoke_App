package com.duc.karaoke_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duc.karaoke_app.data.local.entity.FavoriteSongEntity

@Dao
interface FavoriteSongsDao {
    @Query("SELECT * FROM favorite_songs")
    suspend fun getAllFavoriteSongs(): List<FavoriteSongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(song: FavoriteSongEntity)

    @Query("UPDATE favorite_songs SET localAudioPath = :path WHERE songId = :songId")
    suspend fun updateLocalAudioPath(songId: Int, path: String)

    @Query("DELETE FROM favorite_songs WHERE songId = :songId")
    suspend fun deleteFavoriteById(songId: Int)

    @Query("DELETE FROM favorite_songs")
    suspend fun clearAllFavorites()

    @Query("SELECT *FROM favorite_songs WHERE songId = :songId LIMIT 1")
    suspend fun getFavoriteById( songId: Int): FavoriteSongEntity?
}