package com.duc.karaoke_app.data.local.database

import android.content.Context
import androidx.room.Database
import com.duc.karaoke_app.data.local.dao.FavoriteSongsDao
import com.duc.karaoke_app.data.local.entity.FavoriteSongEntity
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteSongEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoriteSongsDao(): FavoriteSongsDao
}

object DatabaseBuilder {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase{
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "karaoke_app_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}