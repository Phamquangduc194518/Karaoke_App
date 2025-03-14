package com.duc.karaoke_app.data.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.CommentVideo
import com.duc.karaoke_app.data.model.CommentVideoDone
import com.duc.karaoke_app.data.model.GoogleDriveFile
import com.duc.karaoke_app.data.model.Lyric
import com.duc.karaoke_app.data.model.RecordedSongs
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.Video
import com.duc.karaoke_app.ui.adapter.AlbumAdapter
import com.duc.karaoke_app.ui.adapter.CommentPostAdapter
import com.duc.karaoke_app.ui.adapter.CommentVideoAdapter
import com.duc.karaoke_app.ui.adapter.DuetSongAdapter
import com.duc.karaoke_app.ui.adapter.FamousPersonAdapter
import com.duc.karaoke_app.ui.adapter.NewsFeedAdapter
import com.duc.karaoke_app.ui.adapter.PlayListAdapter
import com.duc.karaoke_app.ui.adapter.SlideAdapter
import com.duc.karaoke_app.ui.adapter.TopSongAdapter
import com.duc.karaoke_app.ui.adapter.ViewDuetSongAdapter
import com.duc.karaoke_app.ui.fragment.NewsFeed
import com.duc.karaoke_app.utils.GoogleSignInHelper
import com.duc.karaoke_app.utils.SingleLiveEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.http.FileContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class MusicPlayerViewModel(private val repository: Repository, application: Application) :
    AndroidViewModel(application) {

    private val _song = MutableLiveData<Songs>()
    val song: LiveData<Songs> get() = _song

    private val _video = MutableLiveData<Video>()
    val video: LiveData<Video> get() = _video

    private val _lyricSong = MutableLiveData<List<Lyric>>()
    val lyricSong: LiveData<List<Lyric>> get() = _lyricSong

    private lateinit var exoPlayer: ExoPlayer

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying


    private val _navigateBack = MutableLiveData<Boolean>(false)
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    private val _isNavigate = MutableLiveData<Boolean>(false)
    val isNavigate: LiveData<Boolean> get() = _isNavigate

    private var _toKenToMusicPlayer = ""

    private val _isUploading = MutableLiveData(false)
    val isUploading: LiveData<Boolean>
        get() = _isUploading

    var titlePost = MutableLiveData("")
    var recordingPath = MutableLiveData("")

    var comment = MutableLiveData("")
    var videoId = MutableLiveData<Int>()

    private val _commentList = MutableLiveData<List<CommentVideoDone>>()
    val commentList: LiveData<List<CommentVideoDone>> get() = _commentList


    // LayoutManager cho RecyclerView
    val viewDuetSongManager = LinearLayoutManager(application)

    // Adapter cho RecyclerView
    val viewDuetSongAdapter = ViewDuetSongAdapter()
    val commentAdapter = CommentVideoAdapter()

    init {
        saveTokenToMusicPlayerActivity()
    }

    fun initNewExoPlayer() {
        exoPlayer = ExoPlayer.Builder(getApplication()).build()
    }

    fun onBackPressed() {
        _navigateBack.value = true
        releaseExoPlayer()
    }

    fun setSong(song: Songs) {
        _song.value = song
        Log.d("MusicPlayerViewModel", "Song set: ${song.title}")
    }

    fun setVideo(video: Video) {
        _video.value = video
        Log.d("MusicPlayerViewModel", "Video set: ${video.title}")
    }


    fun playSong(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        if (url.isEmpty()) {
            Log.e("ExoPlayer", "Lỗi: Đường dẫn nhạc rỗng!")
            return
        }
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        _isPlaying.value = true
    }

    fun pauseSong() {
        exoPlayer.pause()
        _isPlaying.value = false
    }

    fun togglePlayPause(url: String) {
        if (!::exoPlayer.isInitialized || exoPlayer.playbackState == Player.STATE_IDLE) { // Kiểm tra nếu exoPlayer đã được khởi tạo
            Log.e("ExoPlayer", "Lỗi: ExoPlayer chưa được khởi tạo! Khởi tạo lại...")
            initNewExoPlayer() // ✅ Tạo mới ExoPlayer nếu nó bị null
        }
        if (_isPlaying.value == true) {
            pauseSong()
        } else {
            if (exoPlayer.currentMediaItem == null || exoPlayer.mediaItemCount == 0) {
                // Nếu chưa có MediaItem, thêm và phát nhạc
                playSong(url)
            } else {
                // Nếu đã có MediaItem, tiếp tục phát
                exoPlayer.play()
                _isPlaying.value = true
            }
        }
    }

    fun releaseExoPlayer() {
        if (::exoPlayer.isInitialized) {
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
            exoPlayer.release()
            _isPlaying.value = false
            Log.d("ExoPlayer", "ExoPlayer đã được giải phóng hoàn toàn!")
        }
    }


    private fun saveTokenToMusicPlayerActivity(): String? {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        if (token != null) {
            Log.e("Token To MusicPlayer", "$token")
            _toKenToMusicPlayer = token.trim()
        } else {
            Log.e("Token To MusicPlayer", "Token không tồn tại")
        }
        return _toKenToMusicPlayer
    }

    private suspend fun uploadFileToDrive(): String? {
        val account = GoogleSignInHelper.getSignedInAccount()
        if (account == null) {
            return null
            Log.e("Upload", "Lỗi: Chưa đăng nhập Google")
        } else {
            Log.e("Upload", "Tài khoản Google: ${account.email}")
        }
        val driveService = GoogleSignInHelper.getGoogleDriveService(account)

        if (driveService == null) {
            Log.e("Upload", "Lỗi: Không thể kết nối với Google Drive")
            return null
        }
        val folderId = "1sek-KlPDD0HXqqsTy6phX3yunky_N6pl" // ID folder trên Google Drive
        val filePath = "/storage/emulated/0/Android/data/com.duc.karaoke_app/cache/recording.mp4"
        val file = java.io.File(filePath)
        if (!file.exists()) {
            Log.e("Upload", "Lỗi: File không tồn tại")
        }

        val formattedFileName = "${1}_${_song.value?.title}.mp4"

        val fileMetadata = com.google.api.services.drive.model.File().apply {
            name = formattedFileName
            parents = listOf(folderId)
        }
        val mediaContent = FileContent("video/mp4", file)
        return withContext(Dispatchers.IO){
            try {
                val uploadedFile = driveService.files()?.create(fileMetadata, mediaContent)
                    ?.setFields("id, name, webViewLink, webContentLink")
                    ?.execute()
                val fileId = uploadedFile?.id
                val fileLink = "https://drive.google.com/uc?export=download&id=$fileId"
                withContext(Dispatchers.Main) {
                    recordingPath.value = fileLink
                    Log.d("Upload", "File public link: $fileLink")
                }
                fileLink
            } catch (e: Exception) {
                Log.e("UploadError", "Lỗi khi tải file lên", e)
                null
            }
        }
    }

    private fun createRecordedSongs() {
        viewModelScope.launch {
            val request = RecordedSongs(
                songName = song.value?.title ?: "",
                recordingPath = recordingPath.value ?: "",
                title = titlePost.value ?: ""
            )
            try {
                val response = repository.createRecordedSong("Bearer $_toKenToMusicPlayer", request)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.e("Tạo bản ghi thành công", "$apiResponse")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Bản ghi", "Lỗi: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("bản ghi", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun postCoverToServer() {
        viewModelScope.launch {
            val uploadedFileLink = uploadFileToDrive()
            if(uploadedFileLink != null){
                recordingPath.value = uploadedFileLink
                createRecordedSongs()
                _isNavigate.value=true
            }else{
                Log.e("Upload", "Lỗi: Không thể lấy file link từ Google Drive!")
            }
        }
    }

    fun getDuetLyric(){
        viewModelScope.launch {
            try{
                if (_song.value == null) {
                    Log.e("Lyric_Duet", "Lỗi: _song.value bị NULL trước khi gọi API")
                    return@launch
                }
                val response = repository.getDuetLyric(_song.value!!.title)
                Log.e("Lyric_Duet_Title", response.isSuccessful.toString())
                if(response.isSuccessful){
                    Log.e("Lyric_Duet_Respone", response.body().toString())
                    _lyricSong.value = response.body()
                    viewDuetSongAdapter.updateLyricDuetSong(_lyricSong.value ?: listOf())
                    Log.e("Lyric_Duet_Respone", _lyricSong.value.toString())
                }else{
                    Log.e("Lyric_Duet","Lỗi: ${response.code()} - ${response.message()}")
                }
            }catch(e: Exception){
                Log.e("Lyric_Duet", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun createCommentVideo (){
        viewModelScope.launch {
            try {
                val request = CommentVideo(
                    videoId = _video.value?.videoId ?: 0,
                    commentText = comment.value ?: ""
                )
                Log.e("comment", "$request")
                val response = repository.createCommentVideo("Bearer $_toKenToMusicPlayer", request)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.e("Tạo comment thành công", "$apiResponse")
                    getCommentVideo()
                    comment.value=""
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Comment", "Lỗi: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("Comment", "Lỗi kết nối: ${e.message}")
            }
        }
    }
    fun getCommentVideo(){
        viewModelScope.launch {
            try {
                val response = repository.getCommentVideo(_video.value?.videoId ?: 0)
                if (response.isSuccessful) {
                    _commentList.value = response.body()
                    commentAdapter.updateCommentLists(response.body() ?: listOf())
                    Log.e("commentDone", "${_commentList.value}")
                } else {
                    Log.e("Comment", "Lỗi kết nối")
                }
            } catch (e: Exception) {
                Log.e("Comment", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}

