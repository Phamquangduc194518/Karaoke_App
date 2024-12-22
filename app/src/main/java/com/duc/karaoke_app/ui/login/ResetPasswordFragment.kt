package com.duc.karaoke_app.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {
    private lateinit var viewmodelLogin: ViewModelLogin
    private lateinit var resetPasswordBinding: FragmentResetPasswordBinding
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
        viewmodelLogin = ViewModelProvider(this, viewModelFactory).get(ViewModelLogin::class.java)
        resetPasswordBinding = FragmentResetPasswordBinding.inflate(layoutInflater)
        resetPasswordBinding.viewModelLogin2= viewmodelLogin
        resetPasswordBinding.lifecycleOwner= viewLifecycleOwner
        return resetPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}