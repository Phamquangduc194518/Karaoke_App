package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentStickerChooserBinding
import com.duc.karaoke_app.databinding.FragmentStickerVideoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class StickerVideoFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentStickerVideoBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStickerVideoBinding.inflate(layoutInflater)
        binding.viewModelStickerVideo = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvSticker.layoutManager= GridLayoutManager(requireContext(), 4)

        binding.ivClose.setOnClickListener{
            fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
        }
        viewModel.isSelectSticker.observe(viewLifecycleOwner){isSelectSticker->
            if(isSelectSticker){
                viewModel.resetIsSelectSticker()
                dismiss()
            }
        }

    }

    companion object {
        fun newInstance(): StickerVideoFragment {
            return StickerVideoFragment()
        }
    }

}