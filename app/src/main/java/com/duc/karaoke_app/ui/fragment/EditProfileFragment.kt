package com.duc.karaoke_app.ui.fragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private lateinit var viewModel: ViewModelLogin
    private lateinit var editProfileBinding: FragmentEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireActivity().application
        val repository= Repository()
        val viewModelFactory = ViewModelFactory(repository,application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelLogin::class.java]
        editProfileBinding= FragmentEditProfileBinding.inflate(layoutInflater)
        editProfileBinding.viewModelEditProfile= viewModel
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
        viewModel.toastMessage.observe(viewLifecycleOwner){messege->
            messege.let{
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

}