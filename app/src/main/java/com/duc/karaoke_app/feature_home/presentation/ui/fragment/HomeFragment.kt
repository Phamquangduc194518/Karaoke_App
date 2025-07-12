package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentHomeBinding
import com.duc.karaoke_app.feature_chat.presentation.ui.MessengerActivity
import com.duc.karaoke_app.feature_favorite.data.FavoriteSongsRepository
import com.duc.karaoke_app.feature_favorite.presentation.viewmodel.FavoriteSongsViewModel
import com.duc.karaoke_app.feature_favorite.presentation.viewmodel.FavoriteSongsViewModelFactory
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome
import com.duc.karaoke_app.feature_player.presentation.ui.MusicPlayerActivity

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    private val favVM: FavoriteSongsViewModel by activityViewModels {
        FavoriteSongsViewModelFactory(
            requireActivity().application,
            FavoriteSongsRepository(requireContext()),
            viewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding.viewModelHome = viewModel
        homeBinding.lifecycleOwner = viewLifecycleOwner
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.images.observe(viewLifecycleOwner) {
            setupAutoSlide(homeBinding.vp2Slide)
        }
        favVM.loadFavoriteSongs()
        viewModel.getIsFavoriteToSongID()
        viewModel.getFollowNotification()
        viewModel.unreadMessage()
        viewModel.userProfile()
        homeBinding.recyclerViewTopSong.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homeBinding.recyclerViewPlayList.layoutManager = LinearLayoutManager(requireContext())
        homeBinding.recyclerFamousPerson.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homeBinding.recyclerAlbum.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homeBinding.recyclerViewRecommendedSongs.layoutManager =
            LinearLayoutManager(requireContext())

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
        viewModel.unreadNotifications()

        viewModel.selectedUserLiveStream.observe(viewLifecycleOwner) { user ->
            user.let {
                Log.e("HomeFragment", "Selected user: ${user}")
                val intent = Intent(requireActivity(), MusicPlayerActivity::class.java).apply {
                    putExtra("FRAGMENT_KEY", "Watch_Live_Fragment")
                    putExtra("UserLiveStream", user)
                }
                startActivity(intent)
            }
        }


        viewModel.albumClick.observe(viewLifecycleOwner) { album ->
            album.let {
                viewModel.getSongsByAlbum()
            }
        }

        viewModel.albumSongDetail.observe(viewLifecycleOwner) { albumDetail ->
            if (albumDetail != null) {
                Log.d("API_RESPONSE", "Album Detail: $albumDetail")
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AlbumDetailsFragment())
                    .commit()
            }
        }

        viewModel.navigateToAllSongs.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AllSongsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        viewModel.isClickSearch.observe(viewLifecycleOwner) { isClick ->
            if (isClick) {
                viewModel.resetClickSearch()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SearchResultFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        viewModel.isClickNotification.observe(viewLifecycleOwner) { isClick ->
            if (isClick) {
                viewModel.resetClickNotification()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, NotificationFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        viewModel.notificationsCount.observe(viewLifecycleOwner) { count ->
            if (count != 0) {
                homeBinding.tvBadge.visibility = View.VISIBLE
            } else {
                homeBinding.tvBadge.visibility = View.GONE
            }
        }

        viewModel.messNotificationsCount.observe(viewLifecycleOwner) { count ->
            if (count != 0) {
                homeBinding.tvBadOfMessages.visibility = View.VISIBLE
            } else {
                homeBinding.tvBadOfMessages.visibility = View.GONE
            }
        }

        homeBinding.ivMessages.setOnClickListener {
            val intent = Intent(requireActivity(), MessengerActivity::class.java).apply {
                putExtra("MESSAGE_KEY", "MESSAGE_ROOM")
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Cập nhật lại danh sách người dùng và trạng thái livestream khi quay lại màn hình chính
        viewModel.getProfileStar()
    }

    private fun setupAutoSlide(viewPager: ViewPager2) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val itemCount = viewPager.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    val currentItem = viewPager.currentItem
                    val nextItem = (currentItem + 1) % itemCount
                    viewPager.setCurrentItem(nextItem, true)
                }
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)


    }
}