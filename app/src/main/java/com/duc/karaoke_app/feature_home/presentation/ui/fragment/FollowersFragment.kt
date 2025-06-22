package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentFollowersBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome

class FollowersFragment : Fragment() {
    private lateinit var followerBinding: FragmentFollowersBinding
    private val viewmodel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followerBinding= FragmentFollowersBinding.inflate(layoutInflater)
        followerBinding.viewModelFollower= viewmodel
        followerBinding.lifecycleOwner = viewLifecycleOwner
        return followerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followerBinding.rcvFollower.layoutManager = LinearLayoutManager(requireContext())

        viewmodel.avatarAndNameClicked.observe(viewLifecycleOwner){ userId ->
            userId?.let {
                viewmodel.checkFollowStatus()
                viewmodel.getFollowers(userId)
                viewmodel.getFollowing(userId)
                viewmodel.getUserInfo()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, UserProfileFragment())
                    .addToBackStack(null)
                    .commit()
                viewmodel.resetAvatarAndNameClicked()
            }
        }
    }
}