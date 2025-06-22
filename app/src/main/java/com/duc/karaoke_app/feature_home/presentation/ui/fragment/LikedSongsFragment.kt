package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentLikedSongsBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome

class LikedSongsFragment : Fragment() {

    private lateinit var fragmentLikedSongsBinding: FragmentLikedSongsBinding
    private val viewModel : ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentLikedSongsBinding= FragmentLikedSongsBinding.inflate(layoutInflater)
        fragmentLikedSongsBinding.likeSongViewModel = viewModel
        fragmentLikedSongsBinding.lifecycleOwner = viewLifecycleOwner
        return fragmentLikedSongsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getIsFavorite()
        fragmentLikedSongsBinding.rcvLikeSongList.layoutManager=
            LinearLayoutManager(requireContext())
    }

}