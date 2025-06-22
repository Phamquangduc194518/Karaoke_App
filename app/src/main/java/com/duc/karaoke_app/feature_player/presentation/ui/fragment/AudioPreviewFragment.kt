package com.duc.karaoke_app.feature_player.presentation.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_player.presentation.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentAudioPreviewBinding
import com.duc.karaoke_app.feature_home.presentation.ui.fragment.MyDialogFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

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
        viewModel.initNewExoPlayer()
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
        viewModel.checkPostingCondition.observe(viewLifecycleOwner){ checkPostingCondition->
            if(checkPostingCondition != true){
                Log.e("checkPostingCondition",checkPostingCondition.toString())
                        val dialog = MyDialogFragment()
                        dialog.show(requireActivity().supportFragmentManager, "MyDialogFragmentTag")
            }
        }
    }

}


