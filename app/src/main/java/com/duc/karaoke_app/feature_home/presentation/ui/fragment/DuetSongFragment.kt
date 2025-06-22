package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentDuetSongBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome
import com.duc.karaoke_app.feature_player.presentation.ui.MusicPlayerActivity

class DuetSongFragment : Fragment() {
    private lateinit var binding: FragmentDuetSongBinding
    private val viewModel: ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDuetSongBinding.inflate(layoutInflater)
        binding.viewModelDuetSong = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvDuetSong.layoutManager= LinearLayoutManager(requireContext())
        viewModel.getIsFavoriteToSongID()
        viewModel.selectedDuetSong.observe(viewLifecycleOwner){song->
            song.let {
                Log.e("DuetSongFragment", "Selected song: ${song}")
                val intent = Intent(requireActivity(), MusicPlayerActivity::class.java).apply {
                    putExtra("FRAGMENT_KEY", "Duet_Song_Fragment")
                    putExtra("Duet_Song", song)
                }
                startActivity(intent)

            }
        }
    }

}