package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentHomeBinding
import com.duc.karaoke_app.ui.adapter.FamousPersonAdapter
import com.duc.karaoke_app.ui.adapter.PlayListAdapter
import com.duc.karaoke_app.ui.adapter.SlideAdapter
import com.duc.karaoke_app.ui.adapter.TopSongAdapter

class HomeFragment : Fragment() {

    private lateinit var viewModel: ViewModelLogin
    private lateinit var homeBinding: FragmentHomeBinding
    val apiKey = "AIzaSyD7L2AC6LciuwBHR9qWt3QQI4qf1gH3Plg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val application = requireActivity().application
        val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelLogin::class.java]
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding.viewModelHome = viewModel
        homeBinding.lifecycleOwner = viewLifecycleOwner
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding.recyclerViewPlayList.layoutManager = LinearLayoutManager(requireContext())
        homeBinding.recyclerViewTopSong.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,// Hướng hiển thị ngang
            false) // Không đảo ngược thứ tự
        homeBinding.recyclerFamousPerson.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.getSongList()
        viewModel.songs.observe(viewLifecycleOwner) { songList ->
            if (songList != null) {
                val adapter = PlayListAdapter(songList)
                homeBinding.recyclerViewPlayList.adapter = adapter
                val adapterTopSong = TopSongAdapter(songList)
                homeBinding.recyclerViewTopSong.adapter = adapterTopSong
            }
        }

        viewModel.images.observe(viewLifecycleOwner) { imageList ->
            if (imageList != null) {
                val adapter = SlideAdapter(imageList)
                homeBinding.vp2Slide.adapter = adapter
                setupAutoSlide(adapter)
            }
        }
        viewModel.loadImageSlide()

        viewModel.userProfileStar.observe(viewLifecycleOwner){userProfileStarList->
            Log.e("Đã chạy recyclerView",userProfileStarList.toString())
            if(userProfileStarList != null){
                val adapterFamousPerson = FamousPersonAdapter(userProfileStarList)
                homeBinding.recyclerFamousPerson.adapter = adapterFamousPerson
            }
        }
        viewModel.getProfileStar()






    }

    private fun setupAutoSlide(adapter: SlideAdapter) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = homeBinding.vp2Slide.currentItem
                val nextItem = if (currentItem == adapter.itemCount - 1) 0 else currentItem + 1
                homeBinding.vp2Slide.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000) // Chuyển slide sau 3 giây
            }
        }
        handler.postDelayed(runnable, 3000)
    }
}