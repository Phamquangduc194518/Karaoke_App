package com.duc.karaoke_app.ui.fragment

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
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
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
        viewModel.getIsFavoriteToSongID()
        homeBinding.recyclerViewTopSong.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        homeBinding.recyclerViewPlayList.layoutManager = LinearLayoutManager(requireContext())
        homeBinding.recyclerFamousPerson.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homeBinding.recyclerAlbum.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

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

        viewModel.albumSongDetail.observe(viewLifecycleOwner){ albumDetail->
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
    }

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
            handler.postDelayed(this, 3000) // Chuyển slide sau 3 giây
        }
    }
    handler.postDelayed(runnable, 3000)
}