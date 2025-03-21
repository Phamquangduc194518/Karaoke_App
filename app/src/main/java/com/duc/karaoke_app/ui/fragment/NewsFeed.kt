package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentNewFeedBinding
import com.google.api.services.drive.model.User


class NewsFeed : Fragment() {

    private lateinit var newsFeedBinding: FragmentNewFeedBinding
    private val viewmodel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newsFeedBinding = FragmentNewFeedBinding.inflate(layoutInflater)
        newsFeedBinding.newsFeedViewModel = viewmodel
        newsFeedBinding.lifecycleOwner = viewLifecycleOwner
        return newsFeedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsFeedBinding.rcvNewFeed.layoutManager = LinearLayoutManager(requireContext())
        viewmodel.getRecordedSongList()
        viewmodel.selectedCommentPost.observe(viewLifecycleOwner) { Comment ->
            if(viewmodel.isSelectCommentPost.value == true){
                Comment.let{
                    val commentFragment = CommentBottomSheetFragment.newInstance()
                    commentFragment.show(childFragmentManager, commentFragment.tag)
                    viewmodel.resetCommentSelection()
                }
            }
        }

        viewmodel.avatarAndNameClicked.observe(viewLifecycleOwner){ userId ->
            userId?.let {
                viewmodel.checkFollowStatus()
                viewmodel.getFollowers(userId)
                viewmodel.getFollowing(userId)
                viewmodel.getUserInfo()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, UserProfileFragment())
                    .addToBackStack(null)
                    .commit()
                viewmodel.resetAvatarAndNameClicked()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewmodel.selectedCommentPost.removeObservers(viewLifecycleOwner)
    }

    override fun onPause() {
        super.onPause()
        viewmodel.pauseAudio() // Tạm dừng nhạc khi Fragment không hiển thị
    }

    override fun onDestroy() {
        super.onDestroy()
        viewmodel.onClearExo()
    }

}