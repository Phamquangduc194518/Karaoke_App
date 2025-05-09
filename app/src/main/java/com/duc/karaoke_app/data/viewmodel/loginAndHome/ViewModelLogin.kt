package com.duc.karaoke_app.data.viewmodel.loginAndHome

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.model.ForgotPasswordRequest
import com.duc.karaoke_app.data.model.LoginRequest
import com.duc.karaoke_app.data.model.RegisterRequest
import com.duc.karaoke_app.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class ViewModelLogin(private val repository: Repository, application: Application):  AndroidViewModel(application){
    private val _navigateToRegister = SingleLiveEvent<Boolean>()
    val navigateToRegister: LiveData<Boolean>
        get() = _navigateToRegister

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _navigateToResetPassword = SingleLiveEvent<Boolean>()
    val navigateToResetPassword: LiveData<Boolean>
        get() = _navigateToResetPassword

    private val _loginSuccess = SingleLiveEvent<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private val _registerSuccess = SingleLiveEvent<Boolean>()
    val registerSuccess: LiveData<Boolean> get() = _registerSuccess

    private val _forGotPassWordSuccess = SingleLiveEvent<Boolean>()
    val forGotPassWordSuccess: LiveData<Boolean> get() = _forGotPassWordSuccess

    fun onResetPasswordClick() {
        _navigateToResetPassword.value = true
    }

    fun onNavigateLoginToRegister() {
        _navigateToRegister.value = true
    }


    var email = MutableLiveData("")
    var password = MutableLiveData("")
    var username = MutableLiveData("")
    var comFirmPass = MutableLiveData("")
    val rememberMe = MutableLiveData<Boolean>()

    // SharedPreferences để lưu trạng thái
    private val sharedPreferences =
        application.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    init{
        loadRememberedData()
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

    fun onRegisterClick() {
        viewModelScope.launch {
            val request = RegisterRequest(
                username = username.value ?: "",
                email = email.value ?: "",
                password = password.value ?: ""
            )
            try {
                if(password.value != comFirmPass.value){
                    _toastMessage.value = "Mật khẩu không khớp!"
                    return@launch
                }
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

    fun onForgotPasswordClick(){
        viewModelScope.launch {
            val request = ForgotPasswordRequest(
                email = email.value ?: ""
            )
            try{
                val response = repository.forgotPassword(request)
                if(response.isSuccessful){
                    val message = response.body()?.message ?: "Email đã được gửi"
                    _toastMessage.value = message
                    _forGotPassWordSuccess.value = true
                }else{
                    _forGotPassWordSuccess.value = false
                }
            }catch (e: Exception) {
                _toastMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("onForgotPasswordClick", "Lỗi kết nối: ${e.message}")
            }
        }

    }
}