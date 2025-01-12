package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.LoginActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.utils.GoogleSignInHelper
import com.duc.karaoke_app.databinding.FragmentProfileBinding
import com.duc.karaoke_app.utils.CustomBottomSheet

class ProfileFragment : Fragment() {
    private lateinit var profileBinding: FragmentProfileBinding
    private val viewModel: ViewModelLogin by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(layoutInflater)
        profileBinding.viewModelProfile= viewModel
        profileBinding.lifecycleOwner= viewLifecycleOwner
        return profileBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileBinding.menuOption.setOnClickListener {
            val bottomSheet = CustomBottomSheet ()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
        profileBinding.iconEdit.setOnClickListener{
            val fragment = EditProfileFragment()
            val transaction= parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }


    }
}