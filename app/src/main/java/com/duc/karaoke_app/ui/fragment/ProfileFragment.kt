package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.LoginActivity
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.utils.GoogleSignInHelper
import com.duc.karaoke_app.databinding.FragmentProfileBinding
import com.duc.karaoke_app.ui.adapter.ProfileAdapter
import com.duc.karaoke_app.utils.CustomBottomSheet
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {
    private lateinit var profileBinding: FragmentProfileBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(layoutInflater)
        profileBinding.viewModelProfile= viewModel
        profileBinding.lifecycleOwner= viewLifecycleOwner
        return profileBinding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userProfile()
        viewModel.getFollowers(viewModel.userProfile.value?.user_id ?:0)
        viewModel.getFollowing(viewModel.userProfile.value?.user_id ?:0)
        val adapter = ProfileAdapter(requireActivity())
        profileBinding.vp2Profile.adapter = adapter

        TabLayoutMediator(profileBinding.tlProfile, profileBinding.vp2Profile) { tab, position ->
            tab.text = when (position) {
                0 -> "Bài hát yêu thích"
                1 -> "Bài viết đã đăng"
                else -> ({}).toString()
            }
        }.attach()
        profileBinding.menuOption.setOnClickListener {
            val bottomSheet = CustomBottomSheet ()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
        profileBinding.iconEdit.setOnClickListener{
            val fragment = EditProfileFragment()
            val transaction= parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
        profileBinding.lottieEffect.playAnimation()

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

        viewModel.selectedSong.observe(viewLifecycleOwner) { song ->
            song.let {
                Log.e("HomeFragment", "Selected song: ${song}")
                val intent = Intent(requireActivity(), MusicPlayerActivity::class.java).apply {
                    putExtra("FRAGMENT_KEY", "Music_Fragment")
                    putExtra("Play_List", song)
                }
                startActivity(intent)

            }
        }


    }
}