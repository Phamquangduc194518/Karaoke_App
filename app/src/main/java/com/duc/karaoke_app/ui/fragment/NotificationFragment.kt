package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentNewFeedBinding
import com.duc.karaoke_app.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {

    private lateinit var notificationBinding: FragmentNotificationBinding
    private val viewmodel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationBinding = FragmentNotificationBinding.inflate(layoutInflater)
        notificationBinding.viewModelNotification = viewmodel
        notificationBinding.lifecycleOwner = viewLifecycleOwner
        return notificationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationBinding.rcvNotifications.layoutManager= LinearLayoutManager(requireContext())

        viewmodel.isReadNotifications.observe(viewLifecycleOwner){notificationId->
            if(notificationId != 0){
                viewmodel.readNotification()
            }
        }
    }
}