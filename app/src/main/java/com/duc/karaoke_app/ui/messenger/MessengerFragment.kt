package com.duc.karaoke_app.ui.messenger

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.ChatRepository
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.data.viewmodel.messenger.ViewModelChat
import com.duc.karaoke_app.data.viewmodel.messenger.ViewModelChatFactory
import com.duc.karaoke_app.databinding.FragmentAlbumDetailsBinding
import com.duc.karaoke_app.databinding.FragmentMessengerBinding
import com.duc.karaoke_app.utils.SocketManager

class MessengerFragment : Fragment() {

    private lateinit var messengerBinding: FragmentMessengerBinding
    private val viewModel: ViewModelChat by activityViewModels {
        ViewModelChatFactory(ChatRepository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        messengerBinding = FragmentMessengerBinding.inflate(layoutInflater)
        messengerBinding.viewModelMessenger = viewModel
        messengerBinding.lifecycleOwner = viewLifecycleOwner
        return messengerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messengerBinding.rvChats.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.itemChatClick.observe(viewLifecycleOwner) { itemId ->
            if (itemId == true) {
                viewModel.resetOnClickChatItem()
                val fragment = ChatHistoryFragment()
                val transaction = parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_chat, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

}