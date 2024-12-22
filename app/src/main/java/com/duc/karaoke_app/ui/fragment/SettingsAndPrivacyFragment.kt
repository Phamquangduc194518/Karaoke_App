package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.LoginActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentSettingsAndPrivacyBinding
import com.duc.karaoke_app.ui.login.LoginFragment

class SettingsAndPrivacyFragment : Fragment() {

    private lateinit var viewModel: ViewModelLogin
    private lateinit var binding: FragmentSettingsAndPrivacyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireActivity().application
        val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository,application)
        viewModel= ViewModelProvider(this,viewModelFactory)[ViewModelLogin::class.java]
        binding = FragmentSettingsAndPrivacyBinding.inflate(layoutInflater)
        binding.viewModelLoginSettingAndPrivacy= viewModel
        binding.lifecycleOwner= viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("kiểm tra" ,viewModel.loginSuccess.toString())
        viewModel.logoutSuccess.observe(viewLifecycleOwner){ navigation->
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner){messege->
            messege.let{
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

}