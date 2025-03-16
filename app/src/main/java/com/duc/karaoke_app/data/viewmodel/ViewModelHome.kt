package com.duc.karaoke_app.data.viewmodel

import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.duc.karaoke_app.data.model.AlbumDetailList
import com.duc.karaoke_app.data.model.Albums
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.LiveStreamRequest
import com.duc.karaoke_app.data.model.NotificationUser
import com.duc.karaoke_app.data.model.Post
import com.duc.karaoke_app.data.model.SearchResponse
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.Sticker
import com.duc.karaoke_app.data.model.UploadAvatarResponse
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserProfile
import com.duc.karaoke_app.data.model.UserResponse
import com.duc.karaoke_app.data.model.Video
import com.duc.karaoke_app.data.model.topSong
import com.duc.karaoke_app.ui.adapter.AlbumAdapter
import com.duc.karaoke_app.ui.adapter.AlbumTrackListAdapter
import com.duc.karaoke_app.ui.adapter.AllSongsAdapter
import com.duc.karaoke_app.ui.adapter.CommentPostAdapter
import com.duc.karaoke_app.ui.adapter.DuetSongAdapter
import com.duc.karaoke_app.ui.adapter.FamousPersonAdapter
import com.duc.karaoke_app.ui.adapter.FollowersAdapter
import com.duc.karaoke_app.ui.adapter.FollowingsAdapter
import com.duc.karaoke_app.ui.adapter.LikeSongListAdapter
import com.duc.karaoke_app.ui.adapter.NewsFeedAdapter
import com.duc.karaoke_app.ui.adapter.NotificationAdapter
import com.duc.karaoke_app.ui.adapter.PlayListAdapter
import com.duc.karaoke_app.ui.adapter.SearchResultAdapter
import com.duc.karaoke_app.ui.adapter.SlideAdapter
import com.duc.karaoke_app.ui.adapter.StickerAdapter
import com.duc.karaoke_app.ui.adapter.TopSongAdapter
import com.duc.karaoke_app.ui.adapter.TopicsAdapter
import com.duc.karaoke_app.utils.BillingManager
import com.duc.karaoke_app.utils.SingleLiveEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar


class ViewModelHome(private val repository: Repository, application: Application) :
    AndroidViewModel(application) {

    private val _isDataLoaded = MutableLiveData<Boolean>()
    val isDataLoaded: LiveData<Boolean> get() = _isDataLoaded

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> get() = _toastMessage


    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean>
        get() = _logoutSuccess

    private val _updateProfileSuccess = MutableLiveData<Boolean>()
    val updateProfileSuccess: LiveData<Boolean> get() = _updateProfileSuccess

    private val _userProfile = MutableLiveData<UserResponse>()
    val userProfile: LiveData<UserResponse>
        get() = _userProfile

    private val _userProfileStar = MutableLiveData<List<User>>()
    val userProfileStar: LiveData<List<User>>
        get() = _userProfileStar

    private val _songs = MutableLiveData<List<Songs>>()
    val songs: LiveData<List<Songs>>
        get() = _songs

    private val _topSongs = MutableLiveData<List<topSong>>()
    val topSongs: LiveData<List<topSong>>
        get() = _topSongs

    private val _duetSongs = MutableLiveData<List<Songs>>()
    val duetSongs: LiveData<List<Songs>>
        get() = _duetSongs

    private val _post = MutableLiveData<List<Post>>()
    val post: LiveData<List<Post>>
        get() = _post

    private val _images = MutableLiveData<List<Int>>()
    val images: LiveData<List<Int>> get() = _images

    private val _album = MutableLiveData<List<Albums>>()
    val album: LiveData<List<Albums>> get() = _album

    private val _commentList = MutableLiveData<List<CommentDone>>()
    val commentList: LiveData<List<CommentDone>> get() = _commentList

    private val _selectedSong = SingleLiveEvent<Songs>()
    val selectedSong: LiveData<Songs> get() = _selectedSong

    private val _selectedDuetSong = SingleLiveEvent<Songs>()
    val selectedDuetSong: LiveData<Songs> get() = _selectedDuetSong

    private val _selectedUserLiveStream = SingleLiveEvent<User>()
    val selectedUserLiveStream: LiveData<User> get() = _selectedUserLiveStream

    private val _likedSongIds = SingleLiveEvent<List<Int>>() // Danh sách bài hát đã thích
    val likedSongIds: LiveData<List<Int>> get() = _likedSongIds

    private val _likedOfAllSongs = SingleLiveEvent<List<Int>>() // Danh sách bài hát đã thích
    val likedOfAllSongs: LiveData<List<Int>> get() = _likedOfAllSongs

    private val _avatarAndNameClicked = SingleLiveEvent<Int?>()
    val avatarAndNameClicked: LiveData<Int?> get() = _avatarAndNameClicked

    private val _albumClick = SingleLiveEvent<Albums>()
    val albumClick: LiveData<Albums> get() = _albumClick

    private val _userProfileData = MutableLiveData<User?>()
    val userProfileData: LiveData<User?> get() = _userProfileData

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> get() = _isFollowing

    private val _followingId = MutableLiveData<Int>()
    val followingId: LiveData<Int> get() = _followingId

    private val _isFollowClick = MutableLiveData<Boolean>()
    val isFollowClick: LiveData<Boolean> get() = _isFollowClick

    private val _selectedVideo = SingleLiveEvent<Video>()
    val selectedVideo: LiveData<Video> get() = _selectedVideo

    private val _navigateToAllSongs = SingleLiveEvent<Boolean>()
    val navigateToAllSongs: LiveData<Boolean> get() = _navigateToAllSongs

    private val billingManager = BillingManager(application.applicationContext)

    // Expose kết quả mua VIP dưới dạng LiveData để View (Fragment) quan sát
    val purchaseSuccess: LiveData<Boolean> get() = billingManager.purchaseSuccess

    private val _isClickVipUpgrade = SingleLiveEvent<Boolean>()
    val isClickVipUpgrade: LiveData<Boolean> get() = _isClickVipUpgrade

    // Expose trạng thái đang tải từ BillingManager
    private val _billingLoading = MutableLiveData<Boolean>()
    val billingLoading: LiveData<Boolean> get() = _billingLoading

    private var exoPlayer: ExoPlayer? = null

    // LiveData theo dõi trạng thái đang phát
    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    // LiveData lưu vị trí của item đang phát (nếu cần dùng để auto pause khi cuộn)
    private val _currentPlayingPosition = MutableLiveData<Int>(-1)
    val currentPlayingPosition: LiveData<Int> get() = _currentPlayingPosition

    //lưu ảnh đã chọn
    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> get() = _selectedImageUri

    //lưu kết quả ảnh load lên
    private val _uploadResult = MutableLiveData<UploadAvatarResponse>()
    val uploadResult: LiveData<UploadAvatarResponse> get() = _uploadResult


    var followerCount = MutableLiveData<Int>()
    var followingCount = MutableLiveData<Int>()

    var profileId = MutableLiveData<Int>()

    //comment
    var comment = MutableLiveData<String>("")
    var stickerUrl = MutableLiveData<String?>() // null nếu không chọn sticker
    var imageUrl = MutableLiveData<String?>()   // null nếu không chọn ảnh
    var post_id = MutableLiveData<Int>()

    private val _albumSongDetail = SingleLiveEvent<AlbumDetailList>()
    val albumSongDetail: LiveData<AlbumDetailList> get() = _albumSongDetail

    private val _isSticker = SingleLiveEvent<Boolean>()
    val isSticker: LiveData<Boolean> get() = _isSticker

    // lưu sticker đã chọn
    private val _selectSticker = MutableLiveData<Sticker>()
    val selectSticker: LiveData<Sticker> get() = _selectSticker

    // kiểm tra xem đã chọn chưa
    private val _isSelectSticker = MutableLiveData<Boolean>()
    val isSelectSticker: LiveData<Boolean> get() = _isSelectSticker

    private val _isNavigate = SingleLiveEvent<Boolean>()
    val isNavigate: LiveData<Boolean> get() = _isNavigate

    // LiveData chứa kết quả tìm kiếm
    val searchResponse = MutableLiveData<SearchResponse?>()

    // LiveData trạng thái loading hoặc lỗi (tuỳ chọn)
    val isLoading = MutableLiveData<Boolean>()

    private val _isClickSearch = MutableLiveData<Boolean>()
    val isClickSearch: LiveData<Boolean> get() = _isClickSearch

    private val _isClickNotification = MutableLiveData<Boolean>()
    val isClickNotification: LiveData<Boolean> get() = _isClickNotification

    // có thông báo hay không?
    private val _hasNotifications = MutableLiveData<Boolean>()
    val hasNotifications: LiveData<Boolean> get() = _hasNotifications

    // nội dung thông báo
    private val _notificationsMessage = MutableLiveData<List<NotificationUser>>()
    val notificationsMessage: LiveData<List<NotificationUser>> get() = _notificationsMessage

    fun resetIsSelectSticker() {
        _isSelectSticker.value = false
    }

    fun setNavigate() {
        _isNavigate.value = true
    }

    fun resetNavigate() {
        _isNavigate.value = false
    }

    fun onClickSearch() {
        _isClickSearch.value = true
    }

    fun resetClickSearch() {
        _isClickSearch.value = false
    }

    fun onClickNotification() {
        _isClickNotification.value = true
    }

    fun resetClickNotification() {
        _isClickNotification.value = false
    }

    fun onSongClicked(song: Songs) {
        _selectedSong.value = song
    }

    fun onDuetSongClicked(song: Songs) {
        _selectedDuetSong.value = song
    }

    fun onFamousClick(user: User) {
        _selectedUserLiveStream.value = user
    }

    fun onAvatarAndNameClicked(userId: Int) {
        _avatarAndNameClicked.value = userId
    }

    fun resetAvatarAndNameClicked() {
        _avatarAndNameClicked.value = null
    }

    fun OnAlbumClick(album: Albums) {
        _albumClick.value = album
    }

    fun onVideoClick(video: Video) {
        _selectedVideo.value = video
    }

    fun onClickVipUpgrade() {
        _isClickVipUpgrade.value = true
    }

    private val _selectedCommentPost = SingleLiveEvent<Post>()
    val selectedCommentPost: LiveData<Post>
        get() = _selectedCommentPost

    private val _isSelectCommentPost = MutableLiveData<Boolean>()
    val isSelectCommentPost: LiveData<Boolean> get() = _isSelectCommentPost

    fun onCommentClicked(post: Post) {
        _selectedCommentPost.value = post
        _isSelectCommentPost.value = true
        post_id.value = post.post_id
        getComments()
    }

    fun resetCommentSelection() {
        _isSelectCommentPost.value = false
    }


    // Khi người dùng ấn mua VIP, gọi hàm này từ Fragment và truyền activity hiện tại
    fun onPurchaseVipClicked(activity: Activity) {
        billingManager.launchPurchaseFlow(activity)
    }

    // Kiểm tra trạng thái VIP (nếu người dùng đã mua trước đó)
    fun checkVipStatus() {
        billingManager.checkVipStatus()
    }

    // Adapter cho RecyclerView
    val playListAdapter = PlayListAdapter()
    val topSongAdapter = TopSongAdapter()
    val famousPersonAdapter = FamousPersonAdapter()
    val albumAdapter = AlbumAdapter()
    val slideViewPagerAdapter = SlideAdapter()
    val newsFeedAdapter = NewsFeedAdapter(this)
    val commentAdapter = CommentPostAdapter()
    val duetSongAdapter = DuetSongAdapter()
    val topicsAdapter = TopicsAdapter()
    val likeSongListAdapter = LikeSongListAdapter()
    val followingsAdapter = FollowingsAdapter()
    val followersAdapter = FollowersAdapter()
    val albumTrackListAdapter = AlbumTrackListAdapter()
    val allSongsAdapter = AllSongsAdapter()
    val stickerAdapter = StickerAdapter()
    val searchResultAdapter = SearchResultAdapter()
    val notificationAdapter =NotificationAdapter()

    var email = MutableLiveData("")
    var password = MutableLiveData("")
    var username = MutableLiveData("")
    var slogan = MutableLiveData("")
    var phone = MutableLiveData("")
    val dateOfBirth = MutableLiveData("01/01/2000")
    var gender = MutableLiveData("")


    private fun getTokenToPreferences(): String? {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    private fun clearTokenFromPreferences() {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("auth_token").apply()
    }

    init {
        // Tải thông tin đã lưu khi khởi tạo ViewModel
        viewModelScope.launch {
            _isDataLoaded.value = false
            try {
                val songListDeferred = async { getSongList() }
                val topSongListDeferred = async { getTopSong() }
                val profileStarDeferred = async { getProfileStar() }
                val allAlbumDeferred = async { getAllAlbum() }
                val imageSlideDeferred = async { loadImageSlide() }
                val recordedSongDeferred = async { getRecordedSongList() }
                val userProfileDeferred = async { userProfile() }
                val duetSongDeferred = async { getDuetSongList() }
                val allTopicsDeferred = async { getAllTopicsWithVideo() }
                val favoriteSongsDeferred = async { getIsFavoriteToSongID() }
                val getFollowNotification = async { getFollowNotification() }


                songListDeferred.await()
                profileStarDeferred.await()
                allAlbumDeferred.await()
                imageSlideDeferred.await()
                recordedSongDeferred.await()
                userProfileDeferred.await()
                duetSongDeferred.await()
                allTopicsDeferred.await()
                favoriteSongsDeferred.await()
                getFollowNotification.await()

                _isDataLoaded.value = true
            } catch (e: Exception) {
                _isDataLoaded.value = true // Đánh dấu hoàn tất ngay cả khi lỗi
            }
        }

        _userProfile.observeForever { profile ->
            Log.e("Check UserProfile", "Profile: $profile")
        }

        playListAdapter.setOnItemClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        topSongAdapter.setOnTopSongClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        albumTrackListAdapter.setOnAlbumTrackClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        allSongsAdapter.setOnItemClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        playListAdapter.setFavoriteClick { songId ->
            val isLiked = likedSongIds.value?.contains(songId) ?: false
            Log.e("check isFavorite", isLiked.toString())
            if (isLiked) {
                removeIsFavorite(songId)
            } else {
                createIsFavorite(songId)
            }
        }

        allSongsAdapter.setFavoriteClick { songId ->
            val isLiked = likedSongIds.value?.contains(songId) ?: false
            Log.e("check isFavorite", isLiked.toString())
            if (isLiked) {
                removeIsFavorite(songId)
            } else {
                createIsFavorite(songId)
            }
        }

        duetSongAdapter.setFavoriteClick { songId ->
            val isLiked = likedSongIds.value?.contains(songId) ?: false
            Log.e("check isFavorite", isLiked.toString())
            if (isLiked) {
                removeIsFavorite(songId)
            } else {
                createIsFavorite(songId)
            }
        }

        stickerAdapter.onClickSticker { sticker ->
            _selectSticker.value = sticker
            if (_selectSticker.value != null) {
                _isSelectSticker.value = true
            }
            Log.e("Sticker đã chọn", _selectSticker.value?.title ?: "")
            stickerUrl.value = _selectSticker.value?.stickerUrl ?: ""

        }

        duetSongAdapter.setOnItemClick { song ->
            onDuetSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        famousPersonAdapter.setOnItemClickOfFamous { user ->
            onFamousClick(user)
        }

        newsFeedAdapter.setOnAvatarAndNameClick { userId ->
            onAvatarAndNameClicked(userId)
        }

        searchResultAdapter.setOnUserClick { userId ->
            onAvatarAndNameClicked(userId)
        }
        searchResultAdapter.setOnItemClick { song ->
            onSongClicked(song)
        }

        albumAdapter.setOnAlbumClick { album ->
            OnAlbumClick(album)
        }
        topicsAdapter.setOnVideoClickListener { video ->
            onVideoClick(video)
        }

        // Quan sát trạng thái đang tải từ BillingManager
        billingManager.isLoading.observeForever { isLoading ->
            _billingLoading.postValue(isLoading)
        }
    }

    fun onDateOfBirthClick(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Chuyển đổi định dạng ngày tháng sang YYYY-MM-DD
                val formattedDate =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                dateOfBirth.value = formattedDate
                Log.e("Formatted Date Set", formattedDate)
            },
            year, month, day
        ).show()
    }

    fun onClickLogOut() {
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            Log.e("token", token)
            val response = repository.logOutUser("Bearer $token")
            if (response.isSuccessful) {
                _logoutSuccess.value = true
                clearTokenFromPreferences()
            } else {
                val errorBody = response.errorBody()?.string()
                _toastMessage.value = "Đăng xuất thất bại: ${errorBody ?: "Lỗi không xác định"}"
                Log.e("Đăng xuất tài khoản", "Lỗi: ${response.code()} - $errorBody")
            }

        }
    }

    fun onClickUpdateProfile() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                val request = UserProfile(
                    username = username.value ?: "",
                    slogan = slogan.value ?: "",
                    password = password.value ?: "",
                    phone = phone.value ?: "",
                    dateOfBirth = dateOfBirth.value ?: "",
                    gender = gender.value ?: ""
                )
                val response = repository.updateUser("Bearer $token", request)
                Log.e("Token hiện tại", token)
                if (response.isSuccessful) {
                    _toastMessage.value = "Cập nhật thành công"
                    _updateProfileSuccess.value = true
                } else {
                    _toastMessage.value = "Cập nhật thất bại: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun userProfile() {
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            try {
                val response = repository.getProfile("Bearer $token")
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                    profileId.postValue(_userProfile.value?.userInfo?.user_id ?: 0)
                    Log.e("UserProfile", _userProfile.value?.userInfo?.user_id.toString())
                    Log.e("UserProfile", "Thông tin:${token}")
                    Log.e("UserProfile", "Thông tin:${response.body()}")
                } else {
                    _toastMessage.value = "Lỗi: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun getSongList() {
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            try {
                val response = repository.getSongList("Bearer $token")
                if (response.isSuccessful) {
                    _songs.value = response.body()
                    playListAdapter.updatePlaylists(_songs.value ?: listOf())
                    allSongsAdapter.updateAllSongsLists(_songs.value ?: listOf())
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun getTopSong() {
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            try {
                val response = repository.getTopSong("Bearer $token")
                if (response.isSuccessful) {
                    _topSongs.value = response.body()
                    topSongAdapter.updateTopSong(_topSongs.value ?: listOf())
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun loadImageSlide() {
        _images.value = repository.getItemSlide()
        slideViewPagerAdapter.updateSlide(_images.value!!)
    }

    fun getProfileStar() {
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            try {
                val response = repository.getProfileStar("Bearer $token")
                if (response.isSuccessful) {
                    _userProfileStar.value = response.body()
                    famousPersonAdapter.updateFamousPerson(_userProfileStar.value ?: listOf())
                } else {
                    _toastMessage.value = "Lỗi: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun getAllAlbum() {
        viewModelScope.launch {
            try {
                val response = repository.getAllAlbum()
                if (response.isSuccessful) {
                    Log.e("getAllAlbum", response.body().toString())
                    _album.value = response.body()
                    albumAdapter.updateAlbums(_album.value ?: listOf())
                } else {
                    _toastMessage.value = "Lỗi: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun getRecordedSongList() {
        viewModelScope.launch {
            try {
                val response = repository.getRecordedSongList()
                if (response.isSuccessful) {
                    _post.value = response.body()
                    newsFeedAdapter.updateRecordedSonglists(post.value ?: listOf())
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun createComment() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                val request = Comment(
                    song_id = post_id.value ?: 0,
                    comment_text = comment.value ?: "",
                    urlSticker = if (stickerUrl.value.isNullOrBlank()) null else stickerUrl.value,
                    urlImage = if (imageUrl.value.isNullOrBlank()) null else imageUrl.value

                )
                Log.e("comment", "$request")
                val response = repository.createComment("Bearer $token", request)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.e("Tạo comment thành công", "$apiResponse")
                    getComments()
                    comment.value = ""
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Comment", "Lỗi: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("Comment", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getComments() {
        viewModelScope.launch {
            try {
                val response = repository.getComments(post_id.value ?: 0)
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

    fun createLiveStream() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                val request = LiveStreamRequest(
                    title = "My LiveStream"
                )
                val response = repository.createLiveStream("Bearer $token", request)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.e("Tạo liveStream thành công", "$apiResponse")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LiveStream", "Lỗi: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("LiveStream", "Lỗi kết nối: ${e.message}")
            }

        }
    }

    fun getDuetSongList() {
        viewModelScope.launch {
            try {
                val response = repository.getDuetSong()
                if (response.isSuccessful) {
                    _duetSongs.value = response.body()
                    duetSongAdapter.updateDuetSongLists(_duetSongs.value ?: listOf())
                }
            } catch (e: Exception) {
                Log.e("Lyric_Duet_Song", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getAllTopicsWithVideo() {
        viewModelScope.launch {
            try {
                val response = repository.getAllTopicsWithVideo()
                if (response.isSuccessful) {
                    topicsAdapter.updateTopicsList(response.body() ?: listOf())
                }
            } catch (e: Exception) {
                Log.e("Lyric_Duet_Song", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun createIsFavorite(songId: Int) {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("isFavorite", "Token hoặc bài hát không hợp lệ")
                    return@launch
                }
                val response = repository.createIsFavorite("Bearer $token", songId)
                if (response.isSuccessful) {
                    Log.d("isFavorite", "Bài hát đã được thêm vào yêu thích")
                    getIsFavoriteToSongID()
                } else {
                    Log.e("isFavorite", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("isFavorite", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun removeIsFavorite(songId: Int) {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("removeFavorite", "Token hoặc bài hát không hợp lệ")
                    return@launch
                }
                val response = repository.removeIsFavorite("Bearer $token", songId)
                if (response.isSuccessful) {
                    Log.d("removeFavorite", "bỏ thích bài háth")
                    getIsFavoriteToSongID()
                } else {
                    Log.e("removeFavorite", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("removeFavorite", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getIsFavoriteToSongID() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("isFavorite", "Token hoặc bài hát không hợp lệ")
                    return@launch
                }
                val response = repository.getIsFavoriteToSongID("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let { likedSongs ->
                        Log.e("favorited", likedSongs.toString())
                        playListAdapter.updateFavorited(likedSongs)
                        allSongsAdapter.updateFavorited(likedSongs)
                        duetSongAdapter.updateFavorited(likedSongs)
                        _likedSongIds.postValue(likedSongs)
                    }
                } else {
                    Log.e("isFavorite", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("isFavorite", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    //hiển thị danh sach bài hát đã thích bên phía profile
    fun getIsFavorite() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("isFavorite", "Token hoặc bài hát không hợp lệ")
                    return@launch
                }
                val response = repository.getIsFavorite("Bearer $token")
                if (response.isSuccessful) {
                    likeSongListAdapter.updateLikeSongLists(
                        response.body()?.favoriteSongs ?: listOf()
                    )
                } else {
                    Log.e("isFavorite", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("isFavorite", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getUserProfile", "Token không hợp lệ")
                    return@launch
                }
                _followingId.postValue(_avatarAndNameClicked.value)
                val response =
                    repository.getUserInfo("Bearer $token", _avatarAndNameClicked.value ?: 0)
                if (response.isSuccessful) {
                    _userProfileData.postValue(response.body())
                } else {
                    Log.e("getUserProfile", "Thất bại: ${response.errorBody()?.string()}")
                    _userProfileData.postValue(null)
                }
            } catch (e: Exception) {
                Log.e("getUserProfile", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun follow() {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("follow", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.follow("Bearer $token", _followingId.value ?: 0)
                if (response.isSuccessful) {
                    _isFollowing.postValue(true)
                    Log.e("follow", "follow thành công")
                }
            }
        } catch (e: Exception) {
            Log.e("follow", "Lỗi kết nối: ${e.message}")
        }
    }

    fun unfollow() {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("unfollow", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.unfollow("Bearer $token", _followingId.value ?: 0)
                if (response.isSuccessful) {
                    _isFollowing.postValue(false)
                }
            }
        } catch (e: Exception) {
            Log.e("unfollow", "Lỗi kết nối: ${e.message}")
        }
    }

    fun checkFollowStatus() {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("checkFollowStatus", "Token không hợp lệ")
                    return@launch
                }
                val response =
                    repository.checkFollowStatus("Bearer $token", _avatarAndNameClicked.value ?: 0)
                if (response.isSuccessful) {
                    _isFollowing.postValue(response.body()?.following)
                    Log.e("checkFollowStatus", response.body()?.following.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("checkFollowStatus", "Lỗi kết nối: ${e.message}")
        }
    }

    fun getFollowers() {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getFollowers", "Token không hợp lệ")
                    return@launch
                }
                val response =
                    repository.getFollowers("Bearer $token", _avatarAndNameClicked.value ?: 0)
                if (response.isSuccessful) {
                    followerCount.postValue(response.body()?.followerCount ?: 0)
                    followersAdapter.updateFollower(response.body()?.followers ?: listOf())
                }
            }
        } catch (e: Exception) {
            Log.e("getFollowers", "Lỗi kết nối: ${e.message}")
        }
    }

    fun getFollowing() {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getFollowing", "Token không hợp lệ")
                    return@launch
                }
                val response =
                    repository.getFollowing("Bearer $token", _avatarAndNameClicked.value ?: 0)
                if (response.isSuccessful) {
                    followingCount.postValue(response.body()?.followingCount ?: 0)
                    followingsAdapter.updateFollowing(response.body()?.following ?: listOf())
                }
            }
        } catch (e: Exception) {
            Log.e("getFollowing", "Lỗi kết nối: ${e.message}")
        }
    }

    fun getFollowersToProfile() {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getFollowersToProfile", "Token không hợp lệ")
                    return@launch
                }
                userProfile.observeForever { profile ->
                    if (profile != null) {
                        val userId = profile.userInfo.user_id ?: return@observeForever
                        Log.e("getFollowersToProfile", userId.toString())

                        viewModelScope.launch {
                            val response = repository.getFollowers("Bearer $token", userId)
                            if (response.isSuccessful) {
                                followerCount.postValue(response.body()?.followerCount ?: 0)
                            }
                        }
                    } else {
                        Log.e("getFollowersToProfile", "userProfile vẫn null")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("getFollowersToProfile", "Lỗi kết nối: ${e.message}")
        }
    }

    fun getFollowingToProfile() {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getFollowingToProfile", "Token không hợp lệ")
                    return@launch
                }
                userProfile.observeForever { profile ->
                    if (profile != null) {
                        val userId = profile.userInfo.user_id ?: return@observeForever
                        Log.e("getFollowingToProfile", userId.toString())

                        viewModelScope.launch {
                            val response = repository.getFollowing("Bearer $token", userId)
                            if (response.isSuccessful) {
                                followingCount.postValue(response.body()?.followingCount ?: 0)
                            }
                        }
                    } else {
                        Log.e("getFollowingToProfile", "userProfile vẫn null")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("getFollowingToProfile", "Lỗi kết nối: ${e.message}")
        }
    }

    fun checkFollowClick() {
        _isFollowClick.postValue(true)
    }

    fun resetCheckFollowClick() {
        _isFollowClick.postValue(false)
    }

    fun getSongsByAlbum() {
        try {
            viewModelScope.launch {
                val response = repository.getSongsByAlbum(_albumClick.value?.id ?: 0)
                if (response.isSuccessful) {
                    albumTrackListAdapter.updateAlbumTrackLists(response.body()?.songs ?: listOf())
                    _albumSongDetail.postValue(response.body())
                } else {
                    Log.e("getSongsByAlbum", "Lỗi: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("getSongsByAlbum", "Lỗi kết nối: ${e.message}")
        }
    }

    fun onMoreClicked() {
        _navigateToAllSongs.value = true // Thông báo rằng cần chuyển đến màn AllSongs
    }

    fun playAudio(audioUrl: String, position: Int) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(getApplication()).build()
        }
        _currentPlayingPosition.value = position
        val mediaItem = MediaItem.fromUri(audioUrl)
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
        _isPlaying.value = true
    }

    fun pauseAudio() {
        exoPlayer?.playWhenReady = false
        _isPlaying.value = false
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        exoPlayer = null
    }


    fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("avatar", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun setSelectedImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun uploadAvatar(file: File) {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("uploadAvatar", "Token không hợp lệ")
                    return@launch
                }
                Log.e("uploadAvatar", token)
                val success = repository.uploadAvatar("Bearer $token", file)
                if (success.isSuccessful) {
                    _uploadResult.value = success.body()
                }
            } catch (e: Exception) {
                Log.e("uploadAvatar", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getStickers() {
        viewModelScope.launch {
            try {
                val sticker = repository.getStickers()
                if (sticker.isSuccessful) {
                    stickerAdapter.upDateSticker(sticker.body() ?: listOf())
                    _isSticker.value = true
                }
            } catch (e: Exception) {
                Log.e("Sticker", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun search(query: String, type: String? = null) {
        if (query.isBlank()) {
            searchResponse.value = null
            return
        }
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.search(query, type)
                val body = response.body()
                searchResponse.value = response.body()
                if (body != null) {
                    val items = mutableListOf<SearchResultAdapter.SearchItem>()
                    body.users?.forEach { user ->
                        items.add(SearchResultAdapter.SearchItem.UserItem(user))
                    }
                    body.songs?.forEach { song ->
                        items.add(SearchResultAdapter.SearchItem.SongItem(song))
                    }
                    searchResultAdapter.updateData(items)
                }
            } catch (e: Exception) {
                Log.e("Search", "Lỗi kết nối: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getFollowNotification() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getFollowingToProfile", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.getFollowNotification("Bearer $token")
                if (response.isSuccessful) {
                    _hasNotifications.value = true
                    _notificationsMessage.value = response.body()?.notificationUser ?: listOf()
                    notificationAdapter.updateDataNotification(response.body()?.notificationUser ?: listOf())
                }
            } catch (e: Exception) {
                Log.e("notification", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}