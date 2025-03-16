package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentAlbumDetailsBinding

class AlbumDetailsFragment : Fragment() {

    private lateinit var albumDetailBinding: FragmentAlbumDetailsBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        albumDetailBinding = FragmentAlbumDetailsBinding.inflate(layoutInflater)
        albumDetailBinding.viewModelAlbumDetail = viewModel
        albumDetailBinding.lifecycleOwner = viewLifecycleOwner
        return albumDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
        )
        albumDetailBinding.rcvAlbumTrackList.layoutManager= LinearLayoutManager(requireContext())

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

        viewModel.isNavigate.observe(viewLifecycleOwner){
            if(viewModel.isNavigate.value == true){
                viewModel.resetNavigate()
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }

}