package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentLikedSongsBinding

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
        fragmentLikedSongsBinding.rcvLikeSongList.layoutManager= LinearLayoutManager(requireContext())
    }

}