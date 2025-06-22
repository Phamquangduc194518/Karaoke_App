package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentEditProfileBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome

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
                viewmodel.uploadAvatar(requireContext(),file)
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
        val genderAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
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
            if (success != null) {
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
                Glide.with(requireContext())
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(editProfileBinding.profileImage)
            }
        }

        viewmodel.updateProfileSuccess.observe(viewLifecycleOwner){ isUpdate->
            if(isUpdate){
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        viewmodel.isNavigate.observe(viewLifecycleOwner){
            if(viewmodel.isNavigate.value == true){
                viewmodel.resetNavigate()
            }
            requireActivity().supportFragmentManager.popBackStack()
        }

        viewmodel.isLoadingUpdateProfile.observe(viewLifecycleOwner){isLoading->
            Log.e("isLoading", isLoading.toString())
            if(isLoading == true){
                editProfileBinding.overlaySuccess.visibility = View.VISIBLE
                editProfileBinding.lottieSuccess.playAnimation()
            }else{
                editProfileBinding.overlaySuccess.visibility = View.GONE
                editProfileBinding.lottieSuccess.cancelAnimation()
            }
        }

    }



}