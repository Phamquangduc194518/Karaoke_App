package com.duc.karaoke_app.feature_chat.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_chat.data.ChatRepository
import com.duc.karaoke_app.feature_chat.presentation.viewmodel.ViewModelChat
import com.duc.karaoke_app.feature_chat.presentation.viewmodel.ViewModelChatFactory
import com.duc.karaoke_app.databinding.FragmentMessengerBinding

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