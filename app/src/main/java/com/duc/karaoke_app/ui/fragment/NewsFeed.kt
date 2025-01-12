package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentNewFeedBinding


class NewsFeed : Fragment() {

    private lateinit var newsFeedBinding: FragmentNewFeedBinding
    private val viewmodel: ViewModelLogin by activityViewModels {
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
        viewmodel.selectedCommentPost.observe(viewLifecycleOwner) { Comment ->
            if(viewmodel.isSelectCommentPost.value == true){
                Comment.let{
                    val commentFragment = CommentBottomSheetFragment.newInstance()
                    commentFragment.show(childFragmentManager, commentFragment.tag)
                    viewmodel.resetCommentSelection()
                }
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewmodel.selectedCommentPost.removeObservers(viewLifecycleOwner)
    }
}