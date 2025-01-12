package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.MainActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentPostAudioBinding
import com.duc.karaoke_app.utils.GoogleSignInHelper

class PostAudioFragment : Fragment() {

    private lateinit var postAudioBinding: FragmentPostAudioBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postAudioBinding = FragmentPostAudioBinding.inflate(layoutInflater)
        postAudioBinding.postAudioViewModel= viewModel
        postAudioBinding.lifecycleOwner= viewLifecycleOwner
        return postAudioBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isNavigate.observe(viewLifecycleOwner){ navigate->
            if(navigate == true){
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }


    }
}