package com.duc.karaoke_app.data.Repository

import android.content.Context
import com.duc.karaoke_app.data.local.dao.FavoriteSongsDao
import com.duc.karaoke_app.data.local.database.DatabaseBuilder
import com.duc.karaoke_app.data.local.entity.FavoriteSongEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteSongsRepository(private val context: Context) {
    private val dao = DatabaseBuilder.getInstance(context.applicationContext).favoriteSongsDao()
    suspend fun getAllFavorites(): List<FavoriteSongEntity> {
        return withContext(Dispatchers.IO) {
            dao.getAllFavoriteSongs()
        }
    }

    suspend fun addFavorite(song: FavoriteSongEntity) {
        withContext(Dispatchers.IO) {
            dao.insertFavorite(song)
        }
    }

    suspend fun updateLocalAudioPath(songId: Int, path: String){
        withContext(Dispatchers.IO) {
            dao.updateLocalAudioPath(songId, path)
        }
    }

    suspend fun removeFavorite(songId: Int) {
        withContext(Dispatchers.IO) {
            dao.deleteFavoriteById(songId)
        }
    }

    suspend fun clearFavorites() {
        withContext(Dispatchers.IO) {
            dao.clearAllFavorites()
        }
    }

    suspend fun getFavoriteById(songId: Int): FavoriteSongEntity ?=
        withContext(Dispatchers.IO) {
            dao.getFavoriteById(songId)
        }
}