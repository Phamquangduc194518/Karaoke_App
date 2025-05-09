package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentSearchResultBinding
import androidx.appcompat.widget.SearchView

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
        searchResultBinding.searchBarCard.apply {
            alpha = 0f
            translationY = -50f
            animate().alpha(1f).translationY(0f).setDuration(300).start()
        }
        searchResultBinding.headerCardView.apply {
            alpha = 0f
            translationY = 30f
            animate().alpha(1f).translationY(0f).setDuration(300).setStartDelay(200).start()
        }
        searchResultBinding.rcvSearchResults.apply {
            alpha = 0f
            animate().alpha(1f).setDuration(300).setStartDelay(300).start()
        }
        fun showLoading(show: Boolean) {
            if (show) {
                searchResultBinding.loadingStateLayout.visibility = View.VISIBLE
                searchResultBinding.loadingStateLayout.alpha = 0f
                searchResultBinding.loadingStateLayout.animate().alpha(1f).setDuration(200).start()
            } else {
                searchResultBinding.loadingStateLayout.animate().alpha(0f).setDuration(200)
                    .withEndAction { searchResultBinding.loadingStateLayout.visibility = View.GONE }.start()
            }
        }
        fun showEmptyState(show: Boolean) {
            if (show) {
                searchResultBinding.emptyStateLayout.visibility = View.VISIBLE
                searchResultBinding.emptyStateCard.apply {
                    alpha = 0f
                    scaleX = 0.8f
                    scaleY = 0.8f
                    animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(300).start()
                }
            } else {
                searchResultBinding.emptyStateCard.animate().alpha(0f).scaleX(0.8f).scaleY(0.8f)
                    .setDuration(200).withEndAction {
                        searchResultBinding.emptyStateLayout.visibility = View.GONE
                    }.start()
            }
        }
        
        // Hiển thị bàn phím khi vào trang search
        searchResultBinding.svSearch.requestFocus()
        
        searchResultBinding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    showLoading(true)
                    viewModel.search(it, "")
                    searchResultBinding.rcvSearchResults.visibility = View.GONE
                    searchResultBinding.txtResultHeader.text = "Kết quả tìm kiếm: \"$it\""
                    searchResultBinding.svSearch.clearFocus()
                    searchResultBinding.root.postDelayed({
                        showLoading(false)
                        searchResultBinding.rcvSearchResults.visibility = View.VISIBLE
                    }, 1000)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    searchResultBinding.rcvSearchResults.visibility = View.GONE
                    searchResultBinding.txtResultHeader.text = "Kết quả tìm kiếm"
                } else {
                    searchResultBinding.rcvSearchResults.visibility = View.VISIBLE
                }
                return true
            }
        })
        
        // Xử lý nút thử lại
        searchResultBinding.btnTryAgain.setOnClickListener {
            searchResultBinding.svSearch.setQuery("", false)
            searchResultBinding.svSearch.requestFocus()
            showEmptyState(false)
        }

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