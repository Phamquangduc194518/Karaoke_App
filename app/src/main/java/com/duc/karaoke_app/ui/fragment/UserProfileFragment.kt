package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.MessengerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment() {

    private lateinit var userProfileBinding: FragmentUserProfileBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    var navigationChat = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userProfileBinding = FragmentUserProfileBinding.inflate(layoutInflater)
        userProfileBinding.viewModelUserProfile = viewModel
        userProfileBinding.lifecycleOwner = viewLifecycleOwner
        return userProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isFollowClick.observe(viewLifecycleOwner) { isFollowClick ->
            if (isFollowClick) {
                viewModel.resetCheckFollowClick()
                val fragment = FollowFragment()
                val transaction = requireActivity().supportFragmentManager
                transaction.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
        }

        viewModel.isNavigate.observe(viewLifecycleOwner){
            if(viewModel.isNavigate.value == true){
                viewModel.resetNavigate()
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .commit()
        }

        viewModel.navigationHistoryChat.observe(viewLifecycleOwner) { navigationHistoryChat ->
            if (navigationHistoryChat == true) {
                viewModel.resetNavigationHistoryChat()
                navigationChat = true
            }
        }

        viewModel.userDataMessage.observe(viewLifecycleOwner) { userData ->
            Log.d("DEBUG", "userProfileData cập nhật: id=${userData}")
            userData?.let{
                if (userData.userId != 0 && navigationChat) {
                    navigationChat = false
                    val intent = Intent(requireContext(), MessengerActivity::class.java).apply {
                        putExtra("MESSAGE_KEY", "MESSAGE_HISTORY")
                        putExtra("userData", userData)
                        Log.e("dữ liệu gửi", userData.toString())
                    }
                    startActivity(intent)
                }
            }
        }
    }

}