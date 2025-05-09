package com.duc.karaoke_app.ui.fragment

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.MutableLiveData
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentProfileBinding
import com.duc.karaoke_app.ui.adapter.ProfileAdapter
import com.duc.karaoke_app.utils.CustomBottomSheet
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {
    private lateinit var profileBinding: FragmentProfileBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private var songIdOnClick = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(layoutInflater)
        profileBinding.viewModelProfile = viewModel
        profileBinding.lifecycleOwner = viewLifecycleOwner
        return profileBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userProfile()
        viewModel.activityStatistics()
        viewModel.getFollowers(viewModel.userProfile.value?.user_id ?: 0)
        viewModel.getFollowing(viewModel.userProfile.value?.user_id ?: 0)
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
            val bottomSheet = CustomBottomSheet()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
        profileBinding.llIconEdit.setOnClickListener {
            val fragment = EditProfileFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
        profileBinding.lottieEffect.playAnimation()
        viewModel.recordedSongAdapter.listenerOnLongClick { songId ->
            showTrashIcon()
            songIdOnClick.value = songId
        }
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

        viewModel.activityStatisticsValue.observe(viewLifecycleOwner) { stats ->
            animateProfileElements {
                animateAllProgressBars(stats)
            }
        }

        profileBinding.trashIcon.setOnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true


                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.setBackgroundResource(R.drawable.bg_trash_hover)
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    view.setBackgroundResource(R.drawable.bg_trash_normal)
                    true
                }

                DragEvent.ACTION_DROP -> {
                    view.setBackgroundResource(R.drawable.bg_trash_normal)
                    AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa bài này không?")
                        .setPositiveButton("Xóa") { _, _ ->
                            viewModel.removeRecordedSong(songIdOnClick.value ?: 0)

                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    view.setBackgroundResource(R.drawable.bg_trash_normal)
                    profileBinding.trashIcon.visibility = View.GONE
                    true
                }

                else -> false
            }
        }

    }

    private fun animateAllProgressBars(stats: com.duc.karaoke_app.data.model.ActivityStatisticsResponse) {
        animateProgressBarSmooth(profileBinding.recordedSongsProgress, stats.coverPostCount*10)
        animateProgressBarSmooth(profileBinding.postsProgress, stats.coverPostCount*10)
        animateProgressBarSmooth(profileBinding.likesProgress, stats.likeCoverCount*10)
        animateProgressBarSmooth(profileBinding.listensProgress, stats.likeSongCount*10)
        animateProgressBarSmooth(profileBinding.commentsProgress, stats.commentCount*10)

    }

    private fun showTrashIcon() {
        profileBinding.trashIcon.apply {
            visibility = View.VISIBLE
            alpha = 0f
            animate().alpha(1f).setDuration(200).start()
        }
    }

    private fun hideTrashIcon() {
        profileBinding.trashIcon.animate().alpha(0f).setDuration(200).withEndAction {
            profileBinding.trashIcon.visibility = View.GONE
        }.start()
    }

    private fun animateProgressBarSmooth(
        pb: ProgressBar,
        targetValue: Int,
        durationMs: Long = 1000L,
    ) {
        val end = targetValue.coerceIn(0, pb.max)
        pb.progress = 0
        ObjectAnimator
            .ofInt(pb, "progress", 0, end)
            .apply {
                duration = durationMs
                interpolator = FastOutSlowInInterpolator()
            }.start()
    }

    private fun animateProfileElements(onComplete: () -> Unit) {
        val slideUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        profileBinding.profileCard.startAnimation(slideUpAnimation)

        profileBinding.statisticsCard.apply {
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(800)
                .setStartDelay(300)
                .start()
        }

        profileBinding.tlProfile.apply {
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(800)
                .setStartDelay(600)
                .start()
        }

        profileBinding.vp2Profile.apply {
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(800)
                .setStartDelay(900)
                .start()
        }

        profileBinding.profileImage.apply {
            scaleX = 0.8f
            scaleY = 0.8f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
        }

        profileBinding.rankBadge.apply {
            scaleX = 0f
            scaleY = 0f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setInterpolator(FastOutSlowInInterpolator())
                .setStartDelay(200)
                .withEndAction {
                    onComplete()
                }
                .start()
        }
    }
}