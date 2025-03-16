package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentAlbumDetailsBinding
import com.duc.karaoke_app.databinding.FragmentStickerChooserBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StickerChooserFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentStickerChooserBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStickerChooserBinding.inflate(layoutInflater)
        binding.viewModelSticker = viewModel
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
        fun newInstance(): StickerChooserFragment {
            return StickerChooserFragment()
        }
    }
}