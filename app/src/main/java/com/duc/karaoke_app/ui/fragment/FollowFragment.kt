package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentFollowBinding
import com.duc.karaoke_app.databinding.FragmentFollowersBinding
import com.duc.karaoke_app.ui.adapter.FollowPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FollowFragment : Fragment() {

    private lateinit var followBinding: FragmentFollowBinding
    private lateinit var followPagerAdapter : FollowPagerAdapter
    private val viewmodel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        followBinding= FragmentFollowBinding.inflate(layoutInflater)
        followBinding.viewModelFollow= viewmodel
        followBinding.lifecycleOwner= viewLifecycleOwner
        return followBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followPagerAdapter = FollowPagerAdapter(this)
        followBinding.viewPager.adapter= followPagerAdapter
        TabLayoutMediator(followBinding.tabLayout, followBinding.viewPager){ tab, position ->
            tab.text = when(position){
                0 -> "Đã Follow"
                1 -> "Follower"
                else -> "Đã Follow"
            }
        }.attach()

        viewmodel.isNavigate.observe(viewLifecycleOwner){
            if(viewmodel.isNavigate.value == true){
                viewmodel.resetNavigate()
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .commit()
        }
    }

}