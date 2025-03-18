package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentAlbumDetailsBinding
import com.duc.karaoke_app.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {

    private lateinit var searchResultBinding: FragmentSearchResultBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchResultBinding = FragmentSearchResultBinding.inflate(layoutInflater)
        searchResultBinding.viewModelSearch = viewModel
        searchResultBinding.lifecycleOwner = viewLifecycleOwner
        return searchResultBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchResultBinding.rcvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        searchResultBinding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // Ví dụ: tìm kiếm theo user; bạn có thể thay đổi tham số "type" thành "song" hoặc null để tìm cả hai
                    viewModel.search(it, "")
                    searchResultBinding.rcvSearchResults.visibility = View.VISIBLE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    searchResultBinding.rcvSearchResults.visibility = View.GONE
                } else {
                    searchResultBinding.rcvSearchResults.visibility = View.VISIBLE
                }
                return true
            }

        })

        viewModel.avatarAndNameClicked.observe(viewLifecycleOwner){ userId ->
            userId?.let {
                viewModel.checkFollowStatus()
                viewModel.getFollowers(userId)
                viewModel.getFollowing(userId)
                viewModel.getUserInfo()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, UserProfileFragment())
                    .addToBackStack(null)
                    .commit()
                viewModel.resetAvatarAndNameClicked()
            }
        }
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