package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentNotificationBinding
import com.google.android.material.tabs.TabLayout

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
        setupUI()
        setupListeners()
        observeViewModel()
    }
    
    private fun setupUI() {
        notificationBinding.rcvNotifications.layoutManager = LinearLayoutManager(requireContext())

        notificationBinding.apply {
            titleTextView.alpha = 0f
            titleTextView.translationY = -50f
            titleTextView.animate().alpha(1f).translationY(0f).setDuration(300).start()
            
            tabLayout.alpha = 0f
            tabLayout.translationY = 50f
            tabLayout.animate().alpha(1f).translationY(0f).setDuration(300).setStartDelay(200).start()
            
            rcvNotifications.alpha = 0f
            rcvNotifications.animate().alpha(1f).setDuration(300).setStartDelay(300).start()
        }

        checkEmptyState()
    }
    
    private fun setupListeners() {
        notificationBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> filterNotifications("all")
                    1 -> filterNotifications("unread")
                    2 -> filterNotifications("read")
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        notificationBinding.searchCardView.setOnClickListener {
            // Implement search functionality here
        }
        viewmodel.isNavigate.observe(viewLifecycleOwner){
            if(viewmodel.isNavigate.value == true){
                viewmodel.resetNavigate()
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
    
    private fun observeViewModel() {
        viewmodel.isReadNotifications.observe(viewLifecycleOwner) { notificationId ->
            if (notificationId != 0) {
                viewmodel.readNotification()
                checkEmptyState()
            }
        }
        checkEmptyState()
    }
    
    private fun refreshNotifications() {
        showLoading(true)
        showLoading(false)
    }
    
    private fun filterNotifications(filter: String) {
        val allNotification = viewmodel.notificationsMessage.value ?: emptyList()

        val filtered = when(filter){
            "all"    -> allNotification
            "unread" -> allNotification.filter { !it.isRead }
            "read" -> allNotification.filter { it.isRead }
            else ->allNotification
        }

        viewmodel.notificationAdapter.updateDataNotification(filtered)
        checkEmptyState()

    }
    
    private fun checkEmptyState() {
        val hasNotifications = viewmodel.notificationAdapter?.itemCount ?: 0 > 0
        
        notificationBinding.apply {
            if (hasNotifications) {
                rcvNotifications.visibility = View.VISIBLE
                emptyStateLayout.visibility = View.GONE
            } else {
                rcvNotifications.visibility = View.GONE
                emptyStateLayout.visibility = View.VISIBLE
            }
        }
    }
    
    private fun showLoading(show: Boolean) {
        notificationBinding.loadingLayout.visibility = if (show) View.VISIBLE else View.GONE
    }
}