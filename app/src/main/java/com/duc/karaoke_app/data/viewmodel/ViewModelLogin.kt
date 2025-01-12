package com.duc.karaoke_app.data.viewmodel

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.data.model.Albums
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.LoginRequest
import com.duc.karaoke_app.data.model.Post
import com.duc.karaoke_app.data.model.RegisterRequest
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserProfile
import com.duc.karaoke_app.data.model.UserResponse
import com.duc.karaoke_app.ui.adapter.AlbumAdapter
import com.duc.karaoke_app.ui.adapter.CommentPostAdapter
import com.duc.karaoke_app.ui.adapter.FamousPersonAdapter
import com.duc.karaoke_app.ui.adapter.NewsFeedAdapter
import com.duc.karaoke_app.ui.adapter.PlayListAdapter
import com.duc.karaoke_app.ui.adapter.SlideAdapter
import com.duc.karaoke_app.ui.adapter.TopSongAdapter
import kotlinx.coroutines.launch
import java.util.Calendar


class ViewModelLogin(private val repository: Repository, application: Application) :
    AndroidViewModel(application) {

    private val _navigateToRegister = MutableLiveData<Boolean>()
    val navigateToRegister: LiveData<Boolean>
        get() = _navigateToRegister

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _navigateToResetPassword = MutableLiveData<Boolean>()
    val navigateToResetPassword: LiveData<Boolean>
        get() = _navigateToResetPassword

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean>
        get() = _logoutSuccess

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> get() = _registerSuccess

    private val _updateProfileSuccess = MutableLiveData<Boolean>()
    val updateProfileSuccess: LiveData<Boolean> get() = _updateProfileSuccess

    private val _userProfile = MutableLiveData<UserResponse>()
    val userProfile: LiveData<UserResponse>
        get()=_userProfile

    private val _userProfileStar = MutableLiveData<List<User>>()
    val userProfileStar: LiveData<List<User>>
        get()=_userProfileStar

    private val _songs = MutableLiveData<List<Songs>>()
    val songs: LiveData<List<Songs>>
        get()= _songs

    private val _post = MutableLiveData<List<Post>>()
    val post: LiveData<List<Post>>
        get()= _post

    private val _images = MutableLiveData<List<Int>>()
    val images: LiveData<List<Int>> get() = _images

    private val _album = MutableLiveData<List<Albums>>()
    val album: LiveData<List<Albums>> get() = _album

    private val _commentList = MutableLiveData<List<CommentDone>>()
    val commentList: LiveData<List<CommentDone>> get() = _commentList

    private val _selectedSong = MutableLiveData<Songs>()
    val selectedSong: LiveData<Songs> get() = _selectedSong

    //comment
    var comment = MutableLiveData("")
    var post_id = MutableLiveData<Int>()

    fun onSongClicked(song: Songs) {
        _selectedSong.value = song
    }

    private val _selectedCommentPost= MutableLiveData<Post>()
    val selectedCommentPost: LiveData<Post>
        get() = _selectedCommentPost

    private val _isSelectCommentPost = MutableLiveData(false)
    val isSelectCommentPost: LiveData<Boolean> get() = _isSelectCommentPost

    fun onCommentClicked(post: Post){
        _selectedCommentPost.value= post
        _isSelectCommentPost.value=true
        post_id.value= post.post_id
        getComments()
    }

    fun resetCommentSelection() {
        _isSelectCommentPost.value = false
    }



    // LayoutManager cho RecyclerView
    val playListLayoutManager = LinearLayoutManager(application)
    val topSongLayoutManagerHorizontal = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
    val famousPersonLayoutManagerHorizontal = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
    val albumLayoutManagerHorizontal = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
//    val newsFeedLayoutManager = LinearLayoutManager(application, LinearLayoutManager.VERTICAL, false)
//    val commentLayoutManager= LinearLayoutManager(application, LinearLayoutManager.VERTICAL, false)

    // Adapter cho RecyclerView
    val playListAdapter = PlayListAdapter()
    val topSongAdapter = TopSongAdapter()
    val famousPersonAdapter = FamousPersonAdapter()
    val albumAdapter = AlbumAdapter()
    val slideViewPagerAdapter = SlideAdapter()
    val newsFeedAdapter = NewsFeedAdapter(this)
    val commentAdapter = CommentPostAdapter()


    var email = MutableLiveData("")
    var password = MutableLiveData("")
    var username = MutableLiveData("")
    var phone = MutableLiveData("")
    val dateOfBirth = MutableLiveData("01/01/2000")
    var gender = MutableLiveData("")
    val rememberMe = MutableLiveData<Boolean>()


    // SharedPreferences để lưu trạng thái
    private val sharedPreferences =
        application.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    init {
        // Tải thông tin đã lưu khi khởi tạo ViewModel
        loadRememberedData()
        getSongList()
        getProfileStar()
        getAllAlbum()
        loadImageSlide()
        getRecordedSongList()
        userProfile()
        _userProfile.observeForever { profile ->
            Log.e("Check UserProfile", "Profile: $profile")
        }

        playListAdapter.setOnItemClick { song ->
            onSongClicked(song) // Gửi sự kiện click vào LiveData
        }
    }

    fun onResetPasswordClick() {
        _navigateToResetPassword.value = true
    }

    fun onNavigateLoginToRegister() {
        _navigateToRegister.value = true
    }


    fun resetNavigation() {
        _navigateToRegister.value = false
        _navigateToResetPassword.value = false
        _loginSuccess.value = false
        _registerSuccess.value = false
        _logoutSuccess.value = false
        _updateProfileSuccess.value=false
    }

    fun isValidEmail(email: String?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email ?: "").matches()
    }

    // Lưu email và trạng thái Remember Me vào SharedPreferences
    private fun saveToPreferences() {
        sharedPreferences.edit().apply {
            putString("email", email.value)
            putBoolean("remember_me", rememberMe.value ?: false)
            apply()
        }
    }

    // Xóa dữ liệu khỏi SharedPreferences
    private fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }

    // Tải dữ liệu đã lưu từ SharedPreferences
    private fun loadRememberedData() {
        email.value = sharedPreferences.getString("email", "")
        rememberMe.value = sharedPreferences.getBoolean("remember_me", false)
    }

    private fun saveTokenToPreferences(token: String) {
        val sharedPreferences =
            getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

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

    fun onDateOfBirthClick(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Chuyển đổi định dạng ngày tháng sang YYYY-MM-DD
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                dateOfBirth.value = formattedDate
                Log.e("Formatted Date Set", formattedDate)
            },
            year, month, day
        ).show()
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            val request = RegisterRequest(
                username = email.value ?: "",
                email = email.value ?: "",
                password = password.value ?: ""
            )
            try {
                val response = repository.registerUser(request)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    _toastMessage.value = apiResponse?.message
                    if (apiResponse?.message?.contains("thành công", ignoreCase = true) == true) {
                        _registerSuccess.value = true
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _toastMessage.value = "Đăng ký thất bại: ${errorBody ?: "Lỗi không xác định"}"
                    Log.e("Đăng ký tài khoản", "Lỗi: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("Đăng ký tài khoản", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            val request = LoginRequest(
                email = email.value ?: "",
                password = password.value ?: ""
            )
            try {
                val response = repository.loginUser(request)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()
                    _toastMessage.value = apiResponse?.message
                    if (rememberMe.value == true) {
                        saveToPreferences()
                    } else {
                        clearPreferences()
                    }
                    if (apiResponse?.message?.contains("thành công", ignoreCase = true) == true) {
                        _loginSuccess.value =
                            true // Đặt loginSuccess thành true nếu đăng nhập thành công
                        val token = apiResponse.token
                        Log.e("Token sau khi đăng nhập", token)
                        saveTokenToPreferences(token)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _toastMessage.value = "Đăng nhập thất bại: ${errorBody ?: "Lỗi không xác định"}"
                    Log.e("Đăng nhập tài khoản", "Lỗi: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("Đăng nhập tài khoản", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun onClickLogOut() {
        viewModelScope.launch {
            val response = repository.logOutUser()
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
                    password = password.value ?: "",
                    phone = phone.value ?: "",
                    dateOfBirth = dateOfBirth.value ?: "",
                    gender = gender.value ?: ""
                )
                val response = repository.updateUser("Bearer $token", request)
                Log.e("Token hiện tại", token)
                if (response.isSuccessful) {
                    _toastMessage.value = "Cập nhật thành công"
                    _updateProfileSuccess.value=true
                } else {
                    _toastMessage.value = "Cập nhật thất bại: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun userProfile(){
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            try{
                val response = repository.getProfile("Bearer $token")
                if(response.isSuccessful){
                    _userProfile.value= response.body()
                    Log.e("UserProfile","Thông tin:${token}")
                    Log.e("UserProfile","Thông tin:${response.body()}")
                }else{
                    _toastMessage.value = "Lỗi: ${response.code()} - ${response.message()}"
                }
            }catch(e: Exception){
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun getSongList(){
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            try{
                val response = repository.getSongList("Bearer $token")
                if(response.isSuccessful){
                    _songs.value= response.body()
                    playListAdapter.updatePlaylists(_songs.value ?: listOf())
                    topSongAdapter.updateTopSong(_songs.value ?: listOf())
                }
            }catch (e:Exception){
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun loadImageSlide(){
        _images.value= repository.getItemSlide()
        slideViewPagerAdapter.updateSlide( _images.value!!)
    }

    fun getProfileStar(){
        viewModelScope.launch {
            val token = getTokenToPreferences().toString().trim()
            try{
                val response = repository.getProfileStar("Bearer $token")
                if(response.isSuccessful){
                    _userProfileStar.value=response.body()
                    famousPersonAdapter.updateFamousPerson(_userProfileStar.value ?: listOf())
                }else{
                    _toastMessage.value = "Lỗi: ${response.code()} - ${response.message()}"
                }
            }catch(e: Exception){
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun getAllAlbum(){
        viewModelScope.launch {
            try{
                val response = repository.getAllAlbum()
                if(response.isSuccessful){
                   Log.e("getAllAlbum", response.body().toString())
                    _album.value = response.body()
                    albumAdapter.updateAlbums(_album.value ?: listOf())
                }else{
                    _toastMessage.value = "Lỗi: ${response.code()} - ${response.message()}"
                }
            }catch(e: Exception){
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun getRecordedSongList(){
        viewModelScope.launch {
            try{
                val response = repository.getRecordedSongList()
                if(response.isSuccessful){
                    _post.value= response.body()
                    newsFeedAdapter.updateRecordedSonglists(post.value?: listOf())
                }
            }catch(e: Exception){
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun createComment(){
        viewModelScope.launch {
            try{
                val token = getTokenToPreferences().toString().trim()
                val request = Comment(
                    song_id = post_id.value ?: 0,
                    comment_text = comment.value ?: ""
                )
                Log.e("comment","$request")
                val response = repository.createComment("Bearer $token", request)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.e("Tạo comment thành công", "$apiResponse")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Comment", "Lỗi: ${response.code()} - $errorBody")
                }
            }catch(e: Exception){
                Log.e("Comment", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getComments(){
        viewModelScope.launch {
            try{
                val response = repository.getComments(post_id.value?: 0)
                if(response.isSuccessful){
                    _commentList.value= response.body()
                    commentAdapter.updateCommentLists(response.body() ?: listOf())
                    Log.e("commentDone","${_commentList.value}")
                }else{
                    Log.e("Comment", "Lỗi kết nối")
                }
            }catch(e: Exception){
                Log.e("Comment", "Lỗi kết nối: ${e.message}")
            }
        }
    }

}