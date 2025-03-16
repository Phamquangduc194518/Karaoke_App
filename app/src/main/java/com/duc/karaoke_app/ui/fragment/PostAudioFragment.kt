package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.MainActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentPostAudioBinding
import com.duc.karaoke_app.utils.GoogleSignInHelper

class PostAudioFragment : Fragment() {

    private lateinit var postAudioBinding: FragmentPostAudioBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Chuyển Uri -> File
            viewModel.setSelectedImageUri(it)
            val file = viewModel.uriToFile(requireContext(), it)
            if (file != null) {
                // Gọi ViewModel để upload
                viewModel.uploadImagePost(file)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postAudioBinding = FragmentPostAudioBinding.inflate(layoutInflater)
        postAudioBinding.postAudioViewModel= viewModel
        postAudioBinding.lifecycleOwner= viewLifecycleOwner
        return postAudioBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isNavigate.observe(viewLifecycleOwner){ navigate->
            if(navigate == true){
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        viewModel.uploadResult.observe(viewLifecycleOwner) { success ->
            if (success.message.contains("thành công")) {
                Toast.makeText(requireContext(), "Upload thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Upload thất bại!", Toast.LENGTH_SHORT).show()
            }
        }

        postAudioBinding.ivCover2.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }

        viewModel.selectedImageUri.observe(viewLifecycleOwner){
                uri ->
            uri?.let {
                // Ví dụ sử dụng Glide để load ảnh
                Glide.with(requireContext())
                    .load(uri)
                    .into(postAudioBinding.ivImagePost)
            }
        }

    }
}