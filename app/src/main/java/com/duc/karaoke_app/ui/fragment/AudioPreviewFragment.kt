package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentAudioPreviewBinding

class AudioPreviewFragment : Fragment() {

    private lateinit var audioPreviewBinding: FragmentAudioPreviewBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        audioPreviewBinding = FragmentAudioPreviewBinding.inflate(layoutInflater)
        audioPreviewBinding.audioPreViewModel = viewModel
        audioPreviewBinding.lifecycleOwner = viewLifecycleOwner
        return audioPreviewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audioPreviewBinding.btnNext.setOnClickListener {
            val fragment = PostAudioFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_music_player, fragment).apply {
                commit()
            }
        }

        audioPreviewBinding.ivClose.setOnClickListener {
            activity?.finish()
        }
    }
}


