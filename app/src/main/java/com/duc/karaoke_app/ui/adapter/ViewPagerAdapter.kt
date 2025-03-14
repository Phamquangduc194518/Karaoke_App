package com.duc.karaoke_app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duc.karaoke_app.ui.fragment.NewsFeed
import com.duc.karaoke_app.ui.fragment.LearnToSingFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0-> NewsFeed()
           1-> LearnToSingFragment()
           else -> NewsFeed()
       }
    }

}