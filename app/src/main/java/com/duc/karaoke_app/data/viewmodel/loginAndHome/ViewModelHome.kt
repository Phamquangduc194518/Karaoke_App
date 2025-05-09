package com.duc.karaoke_app.data.viewmodel.loginAndHome

import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.model.AccountWithFollowers
import com.duc.karaoke_app.data.model.ActivityStatisticsResponse
import com.duc.karaoke_app.data.model.AlbumDetailList
import com.duc.karaoke_app.data.model.Albums
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.DeviceTokenRequest
import com.duc.karaoke_app.data.model.FollowingStar
import com.duc.karaoke_app.data.model.LiveStreamRequest
import com.duc.karaoke_app.data.model.NotificationUser
import com.duc.karaoke_app.data.model.Post
import com.duc.karaoke_app.data.model.SearchResponse
import com.duc.karaoke_app.data.model.SongRequest
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.Sticker
import com.duc.karaoke_app.data.model.UpdateSongStatusRequest
import com.duc.karaoke_app.data.model.UploadAvatarResponse
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserInfo
import com.duc.karaoke_app.data.model.UserProfile
import com.duc.karaoke_app.data.model.VerifyPurchaseRequest
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
import com.duc.karaoke_app.ui.adapter.RecommendedSongAdapter
import com.duc.karaoke_app.ui.adapter.RecordedSongAdapter
import com.duc.karaoke_app.ui.adapter.SearchResultAdapter
import com.duc.karaoke_app.ui.adapter.SlideAdapter
import com.duc.karaoke_app.ui.adapter.StickerAdapter
import com.duc.karaoke_app.ui.adapter.TopSongAdapter
import com.duc.karaoke_app.ui.adapter.TopicsAdapter
import com.duc.karaoke_app.ui.adapter.VideoAdapter
import com.duc.karaoke_app.ui.adapter.WatchLiveAdapter
import com.duc.karaoke_app.utils.BillingManager
import com.duc.karaoke_app.utils.SingleLiveEvent
import com.duc.karaoke_app.utils.SocketManager
import com.github.mikephil.charting.data.BarEntry
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User>
        get() = _userProfile

    private val _userProfileStar = MutableLiveData<List<AccountWithFollowers>>()
    val userProfileStar: LiveData<List<AccountWithFollowers>>
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

    private val _selectedUserLiveStream = SingleLiveEvent<FollowingStar>()
    val selectedUserLiveStream: LiveData<FollowingStar> get() = _selectedUserLiveStream

    private val _likedSongIds = SingleLiveEvent<List<Int>>() // Danh sách bài hát đã thích
    val likedSongIds: LiveData<List<Int>> get() = _likedSongIds

    private val _likedPostIds = SingleLiveEvent<List<Int>>() // Danh sách bài viết đã thích
    val likedPostIds: LiveData<List<Int>> get() = _likedPostIds

    private val _likedOfAllSongs = SingleLiveEvent<List<Int>>() // Danh sách bài hát đã thích
    val likedOfAllSongs: LiveData<List<Int>> get() = _likedOfAllSongs

    private val _avatarAndNameClicked = SingleLiveEvent<Int?>()
    val avatarAndNameClicked: LiveData<Int?> get() = _avatarAndNameClicked

    private val _albumClick = SingleLiveEvent<Albums>()
    val albumClick: LiveData<Albums> get() = _albumClick

    private val _userProfileData = MutableLiveData<User?>()
    val userProfileData: LiveData<User?> get() = _userProfileData

    private val _dataFromUserProfile = MutableLiveData<User?>()
    val dataFromUserProfile: LiveData<User?> get() = _dataFromUserProfile

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> get() = _isFollowing

    private val _followingId = MutableLiveData<Int>()
    val followingId: LiveData<Int> get() = _followingId

    private val _isFollowClick = MutableLiveData<Boolean>()
    val isFollowClick: LiveData<Boolean> get() = _isFollowClick

    private val _selectedTopic = SingleLiveEvent<Int>()
    val selectedTopic: LiveData<Int> get() = _selectedTopic

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

    private val _notificationsCount = MutableLiveData<Int>()
    val notificationsCount: LiveData<Int> get() = _notificationsCount


    private val _isReadNotifications = SingleLiveEvent<Int>()
    val isReadNotifications: LiveData<Int> get() = _isReadNotifications

    private val _barEntries = MutableLiveData<List<BarEntry>>()
    val barEntries: LiveData<List<BarEntry>> get() = _barEntries

    private val _songNames = MutableLiveData<Array<String>>()
    val songNames: LiveData<Array<String>> get() = _songNames

    private val _isVipResponse = MutableLiveData<Boolean>()
    val isVipResponse: LiveData<Boolean> get() = _isVipResponse

    private val _isClickButtonLive = MutableLiveData<Boolean>()
    val isClickButtonLive: LiveData<Boolean> get() = _isClickButtonLive

    private val _isClickButtonQA = MutableLiveData<Boolean>()
    val isClickButtonQA: LiveData<Boolean> get() = _isClickButtonQA

    private val _isClickSendQA = SingleLiveEvent<String>()
    val isClickSendQA: LiveData<String> get() = _isClickSendQA

    private val _isClickVipUpdateOfLiveStream = SingleLiveEvent<Boolean>()
    val isClickVipUpdateOfLiveStream: LiveData<Boolean> get() = _isClickVipUpdateOfLiveStream

    private val _activityStatisticsValue = MutableLiveData<ActivityStatisticsResponse>()
    val activityStatisticsValue: LiveData<ActivityStatisticsResponse> get() = _activityStatisticsValue

    private val _favoriteSongEvent = MutableSharedFlow<Songs>()
    val favoriteSongEvent = _favoriteSongEvent.asSharedFlow()

    private val _removeFavoriteSongEvent = MutableSharedFlow<Songs>()
    val removeFavoriteSongEvent = _removeFavoriteSongEvent.asSharedFlow()

    private var _userDataMessage = SingleLiveEvent<UserInfo>()
    val userDataMessage: LiveData<UserInfo> get() = _userDataMessage

    val titleOptions = listOf("Khiếu nại", "yêu cầu bài hát mới", "Đóng góp ý kiến")

    fun setClickVipUpdateOfLiveStream() {
        _isClickVipUpdateOfLiveStream.value = true
    }

    fun resetIsClickVipUpdateOfLiveStream() {
        _isClickVipUpdateOfLiveStream.value = false
    }

    fun resetIsSelectSticker() {
        _isSelectSticker.value = false
    }

    fun setNavigate() {
        _isNavigate.value = true
    }

    fun resetNavigate() {
        _isNavigate.value = false
    }

    fun setClickButtonQA() {
        _isClickButtonQA.value = true
    }

    fun resetClickButtonQA() {
        _isClickButtonQA.value = false
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

    fun onFamousClick(user: FollowingStar) {
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

    fun onTopicClick(topicId: Int) {
        _selectedTopic.value = topicId
    }

    fun onClickVipUpgrade() {
        _isClickVipUpgrade.value = true
    }

    fun onClickNotificationItem(notificationId: Int) {
        _isReadNotifications.value = notificationId
    }

    fun onButtonLive() {
        _isClickButtonLive.value = true
    }

    fun resetButtonLive() {
        _isClickButtonLive.value = false
    }

    private val _selectedCommentPost = SingleLiveEvent<Post>()
    val selectedCommentPost: LiveData<Post>
        get() = _selectedCommentPost

    private val _isSelectCommentPost = MutableLiveData<Boolean>()
    val isSelectCommentPost: LiveData<Boolean> get() = _isSelectCommentPost

    private val _navigationHistoryChat = MutableLiveData<Boolean>(false)
    val navigationHistoryChat: LiveData<Boolean> get() = _navigationHistoryChat

    fun onCommentClicked(post: Post) {
        _selectedCommentPost.value = post
        _isSelectCommentPost.value = true
        post_id.value = post.post_id
        getComments()
    }

    fun resetCommentSelection() {
        _isSelectCommentPost.value = false
    }

    fun setNavigationHistoryChat(userInfo: User) {
        _navigationHistoryChat.value = true
        _userDataMessage.value = UserInfo(
            userId = userInfo.user_id,
            username = userInfo.username,
            avatarUrl = userInfo.avatarUrl
        )
    }

    fun resetNavigationHistoryChat() {
        _navigationHistoryChat.value = false
    }


    // Khi người dùng ấn mua VIP, gọi hàm này từ Fragment và truyền activity hiện tại
    fun onPurchaseVipClicked(activity: Activity) {
        billingManager.launchPurchaseFlow(activity)
    }

    // Kiểm tra trạng thái VIP (nếu người dùng đã mua trước đó)
    fun checkVipStatus() {
        billingManager.checkVipStatus()
    }

    fun resetQAText() {
        titleQA.value = ""
        contentQA.value = ""
        contactInformationQA.value = ""
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
    val notificationAdapter = NotificationAdapter()
    val watchLiveAdapter = WatchLiveAdapter()
    val recordedSongAdapter = RecordedSongAdapter()
    val videoAdapter = VideoAdapter()
    val recommendedSongAdapter = RecommendedSongAdapter()

    var email = MutableLiveData("")
    var password = MutableLiveData("")
    var username = MutableLiveData("")
    var slogan = MutableLiveData("")
    var phone = MutableLiveData("")
    val dateOfBirth = MutableLiveData("01/01/2000")
    var gender = MutableLiveData("")
    var titleOfLiveStream = MutableLiveData("")

    var titleOfTopic = MutableLiveData("")
    var subTitleOfTopic = MutableLiveData("")
    var durationOfTopic = MutableLiveData("")
    var typeOfTopic = MutableLiveData("")
    var videoCount = MutableLiveData("")

    val titleQA = MutableLiveData("")
    val contentQA = MutableLiveData("")
    val contactInformationQA = MutableLiveData("")


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
                val favoritePostDeferred = async { getIsFavoritePostToSongID() }
                val getFollowNotification = async { getFollowNotification() }
                val recommendedSongsDeferred = async { recommendSongs() }
                val activityStatistics = async { activityStatistics() }

                songListDeferred.await()
                topSongListDeferred.await()
                profileStarDeferred.await()
                allAlbumDeferred.await()
                imageSlideDeferred.await()
                recordedSongDeferred.await()
                userProfileDeferred.await()
                duetSongDeferred.await()
                allTopicsDeferred.await()
                favoriteSongsDeferred.await()
                favoritePostDeferred.await()
                getFollowNotification.await()
                recommendedSongsDeferred.await()
                activityStatistics.await()

                _isDataLoaded.value = true
            } catch (e: Exception) {
                _isDataLoaded.value = true // Đánh dấu hoàn tất ngay cả khi lỗi
            }
        }

        updateDeviceToken()
        _userProfile.observeForever { profile ->
            Log.e("Check UserProfile", "Profile: $profile")
        }

        playListAdapter.setOnItemClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        topSongAdapter.setOnTopSongClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        likeSongListAdapter.setOnItemClick { song ->
            onSongClicked(song)
        }

        albumTrackListAdapter.setOnAlbumTrackClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        allSongsAdapter.setOnItemClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        recommendedSongAdapter.setOnRecommendedSongClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }

        playListAdapter.setFavoriteClick { song ->
            val isLiked = likedSongIds.value?.contains(song.id) ?: false
            Log.e("check isFavorite", isLiked.toString())
            if (isLiked) {
                removeIsFavorite(song.id, song)
            } else {
                createIsFavorite(song.id, song)
            }
        }

        allSongsAdapter.setFavoriteClick { song ->
            val isLiked = likedSongIds.value?.contains(song.id) ?: false
            Log.e("check isFavorite", isLiked.toString())
            if (isLiked) {
                removeIsFavorite(song.id, song)
            } else {
                createIsFavorite(song.id, song)
            }
        }

        duetSongAdapter.setFavoriteClick { song ->
            val isLiked = likedSongIds.value?.contains(song.id) ?: false
            Log.e("check isFavorite", isLiked.toString())
            if (isLiked) {
                removeIsFavorite(song.id, song)
            } else {
                createIsFavorite(song.id, song)
            }
        }

        newsFeedAdapter.setFavoriteClick { postId ->
            val isLiked = _likedPostIds.value?.contains(postId) ?: false
            Log.e("check isFavoritePost", isLiked.toString())
            if (isLiked) {
                removeIsFavoritePost(postId)
            } else {
                createIsFavoritePost(postId)
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

        commentAdapter.setOnAvatarAndNameClick { userId ->
            onAvatarAndNameClicked(userId)
        }

        followersAdapter.setOnItemClick { userId ->
            onAvatarAndNameClicked(userId)
        }

        followingsAdapter.setOnItemClick { userId ->
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
        topicsAdapter.setOnTopicClickListener { TopicId ->
            onTopicClick(TopicId)
        }
        videoAdapter.setOnVideoClickListener { video ->
            onVideoClick(video)
        }

        // Quan sát trạng thái đang tải từ BillingManager
        billingManager.isLoading.observeForever { isLoading ->
            _billingLoading.postValue(isLoading)
        }

        notificationAdapter.setOnItemClick { notificationId ->
            onClickNotificationItem(notificationId) // Gửi sự kiện click vào LiveData
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
                    profileId.postValue(_userProfile.value?.user_id ?: 0)
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
            try {
                val response = repository.getProfileStar()
                if (response.isSuccessful) {
                    val starList = response.body() ?: listOf()
                    withContext(Dispatchers.Main) {
                        _userProfileStar.value = starList
                        famousPersonAdapter.updateFamousPerson(starList)
                    }
                    Log.e("giá trị nhận được", starList.toString())
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
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getRecordedSongList", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.getRecordedSongList("Bearer $token")
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
                    stickerUrl.value = ""
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
                    getRecordedSongList()
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
                    title = titleOfLiveStream.value ?: ""
                )
                val response = repository.createLiveStream("Bearer $token", request)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.e("Tạo liveStream thành công", "$apiResponse")
                    getProfileStar()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LiveStream", "Lỗi: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("LiveStream", "Lỗi kết nối: ${e.message}")
            }

        }
    }

    fun updateLiveStream() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                val response = repository.updateLiveStream("Bearer $token")
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.e("Kết thúc live", "$apiResponse")
                    getProfileStar()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Tắt LiveStream", "Lỗi: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("Tắt LiveStream", "Lỗi kết nối: ${e.message}")
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
                Log.e("getAllTopicsWithVideo", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getAllVideoOfTopic() {
        viewModelScope.launch {
            try {
                val response = repository.getAllVideoOfTopic(_selectedTopic.value ?: 0)
                if (response.isSuccessful) {
                    titleOfTopic.value = response.body()?.title
                    subTitleOfTopic.value = response.body()?.subTitle
                    durationOfTopic.value = response.body()?.duration
                    typeOfTopic.value = response.body()?.type
                    videoCount.value = response.body()?.videos?.size.toString()
                    videoAdapter.updateVideoList(response.body()?.videos ?: listOf())
                }
            } catch (e: Exception) {
                Log.e("getAllVideoOfTopic", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun createIsFavorite(songId: Int, song: Songs) {
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
                    _favoriteSongEvent.emit(song)
                } else {
                    Log.e("isFavorite", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("isFavorite", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun removeIsFavorite(songId: Int, song: Songs) {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("removeFavorite", "Token hoặc bài hát không hợp lệ")
                    return@launch
                }
                val response = repository.removeIsFavorite("Bearer $token", songId)
                if (response.isSuccessful) {
                    Log.d("removeFavorite", "bỏ thích bài hát")
                    getIsFavoriteToSongID()
                    _removeFavoriteSongEvent.emit(song)
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
                    _dataFromUserProfile.postValue(response.body())
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
                    getFollowers(_followingId.value ?: 0)
                    getFollowing(_followingId.value ?: 0)
                    userProfile()
                    getRecordedSongList()
                    getFollowNotification()
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
                    getFollowers(_followingId.value ?: 0)
                    getFollowing(_followingId.value ?: 0)
                    userProfile()
                    getRecordedSongList()
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

    fun getFollowers(userId: Int) {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getFollowers", "Token không hợp lệ")
                    return@launch
                }
                val response =
                    repository.getFollowers("Bearer $token", userId)
                if (response.isSuccessful) {
                    followerCount.postValue(response.body()?.followerCount ?: 0)
                    followersAdapter.updateFollower(response.body()?.followers ?: listOf())
                }
            }
        } catch (e: Exception) {
            Log.e("getFollowers", "Lỗi kết nối: ${e.message}")
        }
    }

    fun getFollowing(userId: Int) {
        try {
            viewModelScope.launch {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getFollowing", "Token không hợp lệ")
                    return@launch
                }
                val response =
                    repository.getFollowing("Bearer $token", userId)
                if (response.isSuccessful) {
                    followingCount.postValue(response.body()?.followingCount ?: 0)
                    followingsAdapter.updateFollowing(response.body()?.following ?: listOf())
                }
            }
        } catch (e: Exception) {
            Log.e("getFollowing", "Lỗi kết nối: ${e.message}")
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

    fun onClearExo() {
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
                    userProfile()
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
                    Log.e("getFollowNotification", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.getFollowNotification("Bearer $token")
                if (response.isSuccessful) {
                    _hasNotifications.value = true
                    _notificationsMessage.value = response.body()?.notificationUser ?: listOf()
                    notificationAdapter.updateDataNotification(
                        response.body()?.notificationUser ?: listOf()
                    )
                }
            } catch (e: Exception) {
                Log.e("notification", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun unreadNotifications() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("unreadNotifications", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.unreadNotifications("Bearer $token")
                if (response.isSuccessful) {
                    _notificationsCount.postValue(response.body()?.notificationUser?.size ?: 0)
                    Log.e(
                        "notification số thông báo",
                        response.body()?.notificationUser?.size.toString()
                    )
                }
            } catch (e: Exception) {
                Log.e("notification", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun readNotification() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getFollowingToProfile", "Token không hợp lệ")
                    return@launch
                }
                val response =
                    repository.readNotification("Bearer $token", _isReadNotifications.value ?: 0)
                if (response.isSuccessful) {

                }
            } catch (e: Exception) {
                Log.e("notification", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun topSongChart() {
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            try {
                val response = repository.getTopSong("Bearer $token")
                if (response.isSuccessful) {
                    val title1 = response.body()?.get(0)?.song?.title ?: ""
                    val title2 = response.body()?.get(1)?.song?.title ?: ""
                    val title3 = response.body()?.get(2)?.song?.title ?: ""
                    val title4 = response.body()?.get(3)?.song?.title ?: ""
                    val title5 = response.body()?.get(4)?.song?.title ?: ""
                    val songNamesArray = arrayOf(title1, title2, title3, title4, title5)
                    _songNames.postValue(songNamesArray)

                    val dataHeart1 = response.body()?.get(0)?.favoriteCount?.toFloat() ?: 0f
                    val dataHeart2 = response.body()?.get(1)?.favoriteCount?.toFloat() ?: 0f
                    val dataHeart3 = response.body()?.get(2)?.favoriteCount?.toFloat() ?: 0f
                    val dataHeart4 = response.body()?.get(3)?.favoriteCount?.toFloat() ?: 0f
                    val dataHeart5 = response.body()?.get(4)?.favoriteCount?.toFloat() ?: 0f
                    val hearts =
                        floatArrayOf(dataHeart1, dataHeart2, dataHeart3, dataHeart4, dataHeart5)

                    val entries = mutableListOf<BarEntry>()
                    for (i in hearts.indices) {
                        entries.add(BarEntry(i.toFloat(), hearts[i]))
                    }
                    _barEntries.postValue(entries)
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun createIsFavoritePost(postId: Int) {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("isFavoritePost", "Token hoặc bài viết không hợp lệ")
                    return@launch
                }
                val response = repository.createIsFavoritePost("Bearer $token", postId)
                if (response.isSuccessful) {
                    Log.d("isFavoritePost", "Bài viết đã được thêm vào yêu thích")
                    getIsFavoritePostToSongID()
                    getRecordedSongList()
                } else {
                    Log.e("isFavoritePost", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("isFavoritePost", "Lỗi kết nối: ${e.message}")
            }
        }
    }


    fun removeIsFavoritePost(songId: Int) {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("removeFavoritePost", "Token hoặc bài viết không hợp lệ")
                    return@launch
                }
                val response = repository.removeIsFavoritePost("Bearer $token", songId)
                if (response.isSuccessful) {
                    Log.d("removeFavoritePost", "bỏ thích bài viết")
                    getIsFavoritePostToSongID()
                    getRecordedSongList()

                } else {
                    Log.e("removeFavoritePost", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("removeFavorite", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getIsFavoritePostToSongID() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("isFavoritePost", "Token hoặc bài viết không hợp lệ")
                    return@launch
                }
                val response = repository.getIsFavoritePostToSongID("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let { likedPost ->
                        Log.e("favorited", likedPost.toString())
                        newsFeedAdapter.updateFavorited(likedPost)
                        _likedPostIds.postValue(likedPost)
                    }
                } else {
                    Log.e("isFavoritePost", "Thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("isFavoritePost", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun updateVipCheck() {
        viewModelScope.launch {
            try {
                val request = VerifyPurchaseRequest(
                    user_id = _userProfile.value?.user_id.toString().trim() ?: "",
                    packageName = "com.duc.karaoke_app",
                    productId = "vip_monthly",
                    purchaseToken = billingManager.purchaseTokenCache ?: ""
                )
                val response = repository.verifyPurchase(request)
                if (response.isSuccessful) {
                    _isVipResponse.value = response.body()?.success
                    Log.e("kết quả tra vip", _isVipResponse.value.toString())
                }
            } catch (e: Exception) {
                Log.e("verifyPurchase", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun songRequestFromUser() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("songRequestFromUser", "Token không hợp lệ")
                    return@launch
                }
                val request = SongRequest(
                    title = titleQA.value ?: "",
                    content = contentQA.value ?: "",
                    contactInformation = contactInformationQA.value ?: ""
                )
                val songRequest = repository.songRequestFromUser("Bearer $token", request)
                if (songRequest.isSuccessful) {
                    resetQAText()
                    _isClickSendQA.value = songRequest.body()?.message ?: ""
                    Log.e("songRequestFromUser", "Gửi đề xuất thành công")
                }
            } catch (e: Exception) {
                Log.e("songRequestFromUser", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getCommentsByStream() {
        viewModelScope.launch {
            try {
                val response = repository.getCommentsByStream(12)
                if (response.isSuccessful) {
                    watchLiveAdapter.updateCommentLists(response.body() ?: listOf())
                }
            } catch (e: Exception) {
                Log.e("getCommentsByStream", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun updateDeviceToken() {
        viewModelScope.launch {
            try {
                val fcmToken = getApplication<Application>()
                    .getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                    .getString("fcm_token", "") ?: ""
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("isFavoritePost", "Token hoặc bài viết không hợp lệ")
                    return@launch
                }
                val request = DeviceTokenRequest(
                    deviceToken = fcmToken
                )
                val response = repository.updateDeviceToken("Bearer $token", request)
                if (response.isSuccessful) {
                    Log.e("updateDeviceToken", "Cập nhật token thành công: $fcmToken")
                }
            } catch (e: Exception) {
                Log.e("updateDeviceToken", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getRecordedSongOfUser() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("getRecordedSongOfUser", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.getRecordedSongOfUser("Bearer $token")
                if (response.isSuccessful) {
                    recordedSongAdapter.updateRecordedSong(response.body() ?: listOf())
                }
            } catch (e: Exception) {
                Log.e("getRecordedSongOfUser", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun makeSongPublic(songId: Int, status: UpdateSongStatusRequest) {
        viewModelScope.launch {
            try {
                val response = repository.makeSongPublic(songId, status)
                if (response.isSuccessful) {
                    getRecordedSongList()
                }
            } catch (e: Exception) {
                Log.e("makeSongPublic", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun removeRecordedSong(songId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.removeRecordedSong(songId)
                if (response.isSuccessful) {
                    getRecordedSongOfUser()
                    getRecordedSongList()
                }
            } catch (e: Exception) {
                Log.e("makeSongPublic", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun recommendSongs() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("recommendSongs", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.recommendSongs("Bearer $token")
                if (response.isSuccessful) {
                    recommendedSongAdapter.updateRecommendedSongs(
                        response.body()?.recommendations ?: listOf()
                    )
                }
            } catch (e: Exception) {
                Log.e("recommendSongs", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun activityStatistics() {
        viewModelScope.launch {
            try {
                val token = getTokenToPreferences().toString().trim()
                if (token.isEmpty()) {
                    Log.e("activityStatistics", "Token không hợp lệ")
                    return@launch
                }
                val response = repository.activityStatistics("Bearer $token")
                if (response.isSuccessful) {
                    _activityStatisticsValue.value = response.body()
                }
            } catch (e: Exception) {
                Log.e("activityStatistics", "Lỗi kết nối: ${e.message}")
            }
        }
    }


}