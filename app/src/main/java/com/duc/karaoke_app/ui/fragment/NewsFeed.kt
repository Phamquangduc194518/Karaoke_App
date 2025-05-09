package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentNewsfeedBinding

class NewsFeed : Fragment() {

    private lateinit var binding: FragmentNewsfeedBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsfeedBinding.inflate(inflater, container, false)
        binding.newsFeedViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.rcvNewFeed.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            ).apply {
            })
            scheduleLayoutAnimation()
        }
        viewModel.getRecordedSongList()
    }

    private fun setupObservers() {
        viewModel.selectedCommentPost.observe(viewLifecycleOwner) { Comment ->
            if(viewModel.isSelectCommentPost.value == true){
                Comment.let{
                    val commentFragment = CommentBottomSheetFragment.newInstance()
                    commentFragment.show(childFragmentManager, commentFragment.tag)
                    viewModel.resetCommentSelection()
                }
            }
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
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.selectedCommentPost.removeObservers(viewLifecycleOwner)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseAudio()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onClearExo()
    }
}