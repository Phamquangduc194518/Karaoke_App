package com.duc.karaoke_app.feature_splash_screen.ui.adapter

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.duc.karaoke_app.R
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duc.karaoke_app.feature_splash_screen.ui.fragment.OnboardingFragment

class OnboardingViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingFragment.newInstance(
                R.string.title_onboarding_1,
                R.string.description_onboarding_1,
                R.raw.mic_permission,
                Manifest.permission.RECORD_AUDIO
            )
            1 -> OnboardingFragment.newInstance(
                R.string.title_onboarding_2,
                R.string.description_onboarding_2,
                R.raw.camera_permission,
                Manifest.permission.CAMERA
            )
            else -> {
                val perm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.POST_NOTIFICATIONS
                } else null
                OnboardingFragment.newInstance(
                    R.string.title_onboarding_3,
                    R.string.description_onboarding_3,
                    R.raw.notification_permission,
                    perm
                )
            }
        }
    }

}

