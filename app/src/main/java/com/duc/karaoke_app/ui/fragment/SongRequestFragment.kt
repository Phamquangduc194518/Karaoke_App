package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentSongRequestBinding

class SongRequestFragment : Fragment() {

    private val viewModel: ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private lateinit var binding: FragmentSongRequestBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongRequestBinding.inflate(layoutInflater)
        binding.viewModelSongRequest = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleOptions = viewModel.titleOptions
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line, titleOptions)
        binding.autoCompleteTitle.setAdapter(adapter)
        binding.autoCompleteTitle.setOnClickListener {
            binding.autoCompleteTitle.showDropDown()
        }
        viewModel.isClickSendQA.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),"Bạn đã gửi yêu cầu thành công", Toast.LENGTH_SHORT).show()
        }
    }
}