package com.duc.karaoke_app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duc.karaoke_app.ui.fragment.FollowersFragment
import com.duc.karaoke_app.ui.fragment.FollowingFragment

class FollowPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FollowingFragment()
            1 -> FollowersFragment()
            else -> FollowingFragment()
        }
    }

}