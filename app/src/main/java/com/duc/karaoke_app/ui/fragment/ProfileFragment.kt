package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.LoginActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.GoogleSignInHelper
import com.duc.karaoke_app.data.viewmodel.LoginRepository
import com.duc.karaoke_app.data.viewmodel.LoginViewModel
import com.duc.karaoke_app.data.viewmodel.LoginViewModelFactory
import com.duc.karaoke_app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var profileBinding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(layoutInflater)
        return profileBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GoogleSignInHelper.initialize(requireActivity())
        profileBinding.tvSignOut.setOnClickListener {
            val intent = Intent(requireActivity(),LoginActivity::class.java)
            startActivity(intent)
            GoogleSignInHelper.signOut(requireActivity())

        }
    }
}