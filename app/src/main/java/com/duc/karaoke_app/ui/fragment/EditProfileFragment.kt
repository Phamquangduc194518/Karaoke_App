package com.duc.karaoke_app.ui.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private lateinit var editProfileBinding: FragmentEditProfileBinding
    private val viewmodel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Chuyển Uri -> File
            viewmodel.setSelectedImageUri(it)
            val file = viewmodel.uriToFile(requireContext(), it)
            if (file != null) {
                // Gọi ViewModel để upload
                viewmodel.uploadAvatar(file)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editProfileBinding= FragmentEditProfileBinding.inflate(layoutInflater)
        editProfileBinding.viewModelEditProfile= viewmodel
        editProfileBinding.lifecycleOwner= viewLifecycleOwner
        // Gắn Adapter cho Spinner
        val genderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Nam", "Nữ", "Giới tính khác")
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editProfileBinding.spinnerGender.adapter = genderAdapter
        return editProfileBinding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.toastMessage.observe(viewLifecycleOwner){messege->
            messege.let{
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewmodel.uploadResult.observe(viewLifecycleOwner) { success ->
            if (success.message.contains("thành công")) {
                Toast.makeText(requireContext(), "Upload thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Upload thất bại!", Toast.LENGTH_SHORT).show()
            }
        }

        editProfileBinding.cameraIcon.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }

        viewmodel.selectedImageUri.observe(viewLifecycleOwner){
                uri ->
            uri?.let {
                // Ví dụ sử dụng Glide để load ảnh
                Glide.with(requireContext())
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(editProfileBinding.profileImage)
            }
        }

        viewmodel.updateProfileSuccess.observe(viewLifecycleOwner){ isUpdate->
            if(isUpdate){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment())
                    .commit()
            }
        }

        viewmodel.isNavigate.observe(viewLifecycleOwner){
            if(viewmodel.isNavigate.value == true){
                viewmodel.resetNavigate()
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .commit()
        }

    }



}