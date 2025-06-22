package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.data.UpdateSongStatusRequest
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentPostedArticlesBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome
import com.duc.karaoke_app.feature_home.presentation.ui.adapter.RecordedSongAdapter

class PostedArticlesFragment : Fragment() {
    private lateinit var postedArticlesBinding: FragmentPostedArticlesBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postedArticlesBinding = FragmentPostedArticlesBinding.inflate(layoutInflater)
        postedArticlesBinding.viewModelPostedArticles= viewModel
        postedArticlesBinding.lifecycleOwner= viewLifecycleOwner
        return postedArticlesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postedArticlesBinding.rcvPostedArticles.layoutManager =
            LinearLayoutManager(requireContext())
        viewModel.getRecordedSongOfUser()

        viewModel.recordedSongAdapter.setOnStatusChangeListener(object:
            RecordedSongAdapter.OnSongStatusChangeListener {
            override fun onStatusChanged(songId: Int, newStatus: String) {
                val request = UpdateSongStatusRequest(status = newStatus)
                viewModel.makeSongPublic(songId, request)
            }

        })
    }
}