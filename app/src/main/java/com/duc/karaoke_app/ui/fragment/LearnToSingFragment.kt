package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentLearnToSingBinding

class LearnToSingFragment : Fragment() {

    private lateinit var learnToSingBinding: FragmentLearnToSingBinding
    private val viewModel: ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        learnToSingBinding= FragmentLearnToSingBinding.inflate(layoutInflater)
        learnToSingBinding.topicViewModel = viewModel
        learnToSingBinding.lifecycleOwner = viewLifecycleOwner
        return learnToSingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        learnToSingBinding.rcvTopicList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.selectedVideo.observe(viewLifecycleOwner){ video->
            if(video != null){
                val intent = Intent(requireContext(), MusicPlayerActivity::class.java).apply {
                    putExtra("FRAGMENT_KEY", "Video_Fragment")
                    putExtra("Video", video)
                }
                startActivity(intent)
            }
        }
    }



}