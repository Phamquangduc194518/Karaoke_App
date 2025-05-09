package com.duc.karaoke_app

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.data.Repository.ChatRepository
import com.duc.karaoke_app.data.Repository.FavoriteSongsRepository
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.model.UserInfo
import com.duc.karaoke_app.data.viewmodel.messenger.ViewModelChat
import com.duc.karaoke_app.data.viewmodel.messenger.ViewModelChatFactory
import com.duc.karaoke_app.data.viewmodel.musicPlayer.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.musicPlayer.MusicPlayerViewModelFactory
import com.duc.karaoke_app.ui.adapter.messenger.ChatHistoryAdapter
import com.duc.karaoke_app.ui.messenger.ChatHistoryFragment
import com.duc.karaoke_app.ui.messenger.MessengerFragment

class MessengerActivity : AppCompatActivity() {
    private lateinit var viewModel : ViewModelChat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = this.application
        val chatRepo = ChatRepository()
        val viewModelFactory = ViewModelChatFactory(chatRepo,application)
        try {
            viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelChat::class.java]
            Log.d("MusicPlayerActivity", "ViewModel initialized: $viewModel")
        } catch (e: Exception) {
            Log.e("MusicPlayerActivity", "Error initializing ViewModel: ${e.message}", e)
        }
        setContentView(R.layout.activity_messenge)

        val fragment = intent.getStringExtra("MESSAGE_KEY")
        val userData = intent.getParcelableExtra<UserInfo>("userData")
        when (fragment) {
            "MESSAGE_HISTORY" -> {
                loadFragment(ChatHistoryFragment())
                if (userData?.userId != 0) {
                    viewModel.setUserIdChat(userData?.userId ?: 0)
                    userData?.let { viewModel.setOtherIdFromHistoryChat(it) }
                    Log.d("MessengerActivity", "Bắt đầu tạo/lấy chat room với userId: $userData?.userId")
                }
            }
            "MESSAGE_ROOM" -> loadFragment(MessengerFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_chat, fragment)
        transaction.commit()
    }
}