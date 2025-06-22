package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentCommentBottomSheetBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCommentBottomSheetBinding
    private val viewModel: ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBottomSheetBinding.inflate(inflater, container, false)
        binding.commentBottomSheetViewModel= viewModel
        binding.lifecycleOwner= viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewComments.layoutManager= LinearLayoutManager(requireActivity())

        viewModel.getComments()
        binding.ivClose.setOnClickListener{
            fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
        }

        viewModel.isSticker.observe(viewLifecycleOwner){ isSticker->
            if(isSticker){
                val commentFragment = StickerChooserFragment.newInstance()
                commentFragment.show(childFragmentManager, commentFragment.tag)
            }
        }
    }

    @SuppressLint("ResourceType")
    override fun onStart() {
        super.onStart()

        val dialog = dialog ?: return
        val bottomSheet = dialog.findViewById<View>(R.layout.fragment_comment_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED // Mở rộng ngay khi hiển thị
            behavior.peekHeight = 500 // Đặt chiều cao theo dp

            // Đặt chiều cao của BottomSheet theo mong muốn
            val layoutParams = it.layoutParams
            layoutParams.height = 500 // Đổi thành chiều cao bạn muốn
            it.layoutParams = layoutParams
        }
    }

    companion object {
        fun newInstance(): CommentBottomSheetFragment {
            return CommentBottomSheetFragment()
        }
    }



}