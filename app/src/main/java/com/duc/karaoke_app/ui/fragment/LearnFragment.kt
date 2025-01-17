package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.duc.karaoke_app.databinding.FragmentLearnBinding
import com.duc.karaoke_app.ui.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LearnFragment : Fragment() {

    private lateinit var learnBinding: FragmentLearnBinding
    private lateinit var pagerAdapter: ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        learnBinding= FragmentLearnBinding.inflate(layoutInflater)
        return learnBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = ViewPagerAdapter(this)
        learnBinding.viewPager.adapter= pagerAdapter
        TabLayoutMediator(learnBinding.tabLayout, learnBinding.viewPager){tab, position ->
            tab.text = when(position){
                0 -> "Bảng Tin"
                1 -> "Bài Giảng"
                else -> "Bảng Tin"
            }
        }.attach()
    }

}