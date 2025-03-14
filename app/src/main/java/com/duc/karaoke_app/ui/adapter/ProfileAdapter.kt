package com.duc.karaoke_app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duc.karaoke_app.ui.fragment.LikedSongsFragment
import com.duc.karaoke_app.ui.fragment.PostedArticlesFragment

class ProfileAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LikedSongsFragment()
            1 -> PostedArticlesFragment()
            else -> LikedSongsFragment()
        }
    }
}