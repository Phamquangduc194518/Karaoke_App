package com.duc.karaoke_app.data.viewmodel.favoriteSongsLocal

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duc.karaoke_app.data.Repository.FavoriteSongsRepository
import com.duc.karaoke_app.data.local.entity.FavoriteSongEntity
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class FavoriteSongsViewModel(
    application: Application,
    private val repository: FavoriteSongsRepository,
    private val viewModelHome: ViewModelHome
) : AndroidViewModel(application) {

    private val _favoriteSongs = MutableStateFlow<List<FavoriteSongEntity>>(emptyList())
    val favoriteSongs: StateFlow<List<FavoriteSongEntity>> = _favoriteSongs

    init {
        Log.d("FavoriteSongsVM", "ViewModel initialized")
        observeFavoriteSongEvent()
    }

    private fun observeFavoriteSongEvent() {
        viewModelScope.launch {
            viewModelHome.favoriteSongEvent.collect { song ->
                Log.d("FavoriteSongsVM", "Received favorite song event: ${song.title}")
                saveFavoriteSongToRoom(song)
            }
        }
        viewModelScope.launch {
            viewModelHome.removeFavoriteSongEvent.collect { song ->
                Log.d("FavoriteSongsVM", "Remove favorite song event: ${song.title}")
                removeFavoriteSongToRoom(song)
            }
        }

    }

    private suspend fun saveFavoriteSongToRoom(song: Songs) {
        val favoriteEntity = FavoriteSongEntity(
            songId = song.id,
            title = song.title,
            subTitle = song.subTitle,
            artist = song.artist,
            lyrics = song.lyrics,
            audioUrl = song.audioUrl,
            coverImageUrl = song.coverImageUrl,
            localAudioPath = null
        )
        repository.addFavorite(favoriteEntity)

        song.audioUrl?.let { url ->
            var file = downloadFile(url, "${song.title}.mp3")
            file?.absolutePath?.let { path ->
                repository.updateLocalAudioPath(song.id, path)
            }
        }
        loadFavoriteSongs()
    }

    fun loadFavoriteSongs() {
        viewModelScope.launch {
            _favoriteSongs.value = repository.getAllFavorites()
            Log.d(
                "FavoriteSongsVM",
                "Current favorites count = ${repository.getAllFavorites()}"
            )
            Log.v("FavoriteSongsVM", "List = $repository.getAllFavorites()")
        }
    }

    fun removeFavoriteSongToRoom(song: Songs) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavorite(song.id)
            Log.d("FavoriteSongsVM", "Removed favorite for songId=${song.id} from DB")

            val dir = getApplication<Application>().filesDir.resolve("songs")
            val file = File(dir, "${song.title}.mp3")
            if (file.exists()) {
                val deleted = file.delete()
                Log.d("FavoriteSongsVM", "Deleted local file=${file.absolutePath}: $deleted")
            }
            loadFavoriteSongs()
        }
    }

    private suspend fun downloadFile(url: String, fileName: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val response = OkHttpClient().newCall(request).execute()
                if (!response.isSuccessful) return@withContext null
                val dir = getApplication<Application>().filesDir.resolve("songs")
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, fileName)
                response.body?.byteStream()?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                file
            } catch (e: Exception) {
                Log.e("Download", "Lỗi tải file", e)
                null
            }
        }
    }
}


