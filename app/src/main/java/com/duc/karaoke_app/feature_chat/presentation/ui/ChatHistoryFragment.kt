package com.duc.karaoke_app.feature_chat.presentation.ui

import android.os.Bundle
import android.util.Log
import com.duc.karaoke_app.R
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.feature_chat.data.ChatRepository
import com.duc.karaoke_app.feature_chat.presentation.viewmodel.ViewModelChat
import com.duc.karaoke_app.feature_chat.presentation.viewmodel.ViewModelChatFactory
import com.duc.karaoke_app.databinding.FragmentChatHistoryBinding
import com.duc.karaoke_app.feature_chat.data.remote.SocketManager

class ChatHistoryFragment : Fragment() {

    private lateinit var chatHistoryBinding: FragmentChatHistoryBinding
    private val viewModel: ViewModelChat by activityViewModels {
        ViewModelChatFactory(ChatRepository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatHistoryBinding = FragmentChatHistoryBinding.inflate(layoutInflater)
        chatHistoryBinding.viewModelChatHistory = viewModel
        chatHistoryBinding.lifecycleOwner = viewLifecycleOwner
        return chatHistoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatHistoryBinding.rvMessages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.getMessages()
        SocketManager.onUserConnected { uid ->
            Log.d("ChatDebug", "User vừa tham gia socket: userId=$uid")
        }

        viewModel.chatHistory.observe(viewLifecycleOwner) {
            chatHistoryBinding.rvMessages.scrollToPosition(it.size - 1)
        }

        chatHistoryBinding.btnBack.setOnClickListener {
            val fragment = MessengerFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_chat, fragment)
            transaction.commit()
        }

    }
}