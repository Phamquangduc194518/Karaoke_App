package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.duc.karaoke_app.R
import com.duc.karaoke_app.databinding.FragmentLearnBinding
import com.duc.karaoke_app.ui.adapter.ViewPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LearnFragment : Fragment() {

    private lateinit var learnBinding: FragmentLearnBinding
    private lateinit var pagerAdapter: ViewPagerAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        learnBinding = FragmentLearnBinding.inflate(layoutInflater)
        return learnBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyAnimations()
        setupViewPager()
        setupTabLayout()
        setupProfileSection()
        setupSwipeRefresh()
    }
    
    private fun applyAnimations() {
        // Simple fade in animation for header elements
        val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeIn.interpolator = AccelerateDecelerateInterpolator()
    }
    
    private fun setupViewPager() {
        pagerAdapter = ViewPagerAdapter(this)
        learnBinding.viewPager.apply { 
            adapter = pagerAdapter
            
            // Optimize touch sensitivity
            val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            val recyclerView = recyclerViewField.get(this)
            val touchSlopField = androidx.recyclerview.widget.RecyclerView::class.java.getDeclaredField("mTouchSlop")
            touchSlopField.isAccessible = true
            val touchSlop = touchSlopField.get(recyclerView) as Int
            touchSlopField.set(recyclerView, touchSlop * 2) // Improve swipe experience
            
            // Preload adjacent pages for smoother transitions
            offscreenPageLimit = 1
        }
    }
    
    private fun setupTabLayout() {
        // Connect TabLayout with ViewPager with enhanced animations
        TabLayoutMediator(learnBinding.tabLayout, learnBinding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Bảng Tin"
                1 -> "Khoá Học"
                else -> "Bảng Tin"
            }
            
            // Add icons to tabs
            tab.icon = when(position) {
                0 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_feed)
                1 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_lesson)
                else -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_feed)
            }
        }.attach()
        
        // Add tab selection animation and update title
        learnBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Animate tab selection
                tab.view.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(200)
                    .start()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Reset animation
                tab.view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Scroll to top when tab is reselected
                learnBinding.viewPager.currentItem = tab.position
            }
        })
        
        // Update the title based on selected tab with animation
        learnBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                
                // Fade out current title
                learnBinding.tvTitle.animate()
                    .alpha(0f)
                    .setDuration(150)
                    .withEndAction {
                        // Update title text
                        learnBinding.tvTitle.text = when(position) {
                            0 -> "Bảng tin"
                            1 -> "Khoá Học"
                            else -> "Bảng tin"
                        }
                        
                        // Update subtitle text
                        learnBinding.tvSubtitle.text = when(position) {
                            0 -> "Hôm nay bạn có gì?"
                            1 -> "Khám phá các khóa học mới"
                            else -> "Hôm nay bạn muốn học gì?"
                        }
                        
                        // Fade in new title
                        learnBinding.tvTitle.animate()
                            .alpha(1f)
                            .setDuration(150)
                            .start()
                            
                        learnBinding.tvSubtitle.animate()
                            .alpha(1f)
                            .setDuration(150)
                            .start()
                    }.start()
            }
        })
    }
    
    private fun setupProfileSection() {
        // Profile image click handling with ripple effect
        learnBinding.profileImageView.apply {
            setOnClickListener {
                // Add ripple animation
                val animator = AnimationUtils.loadAnimation(context, R.anim.pulse)
                startAnimation(animator)
                
                // Show profile options or navigate to profile
                Snackbar.make(learnBinding.root, "Xem thông tin cá nhân", Snackbar.LENGTH_SHORT).show()
            }
        }

    }
    
    private fun setupSwipeRefresh() {
        // Setup pull-to-refresh functionality
        learnBinding.swipeRefreshLayout.apply {
            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.purple_500),
                ContextCompat.getColor(requireContext(), R.color.purple_700)
            )
            
            setOnRefreshListener {
                // Simulate content refresh
                postDelayed({
                    isRefreshing = false
                    Snackbar.make(learnBinding.root, "Đã cập nhật nội dung mới nhất", Snackbar.LENGTH_SHORT).show()
                }, 1000)
            }
        }
    }

}