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
import com.duc.karaoke_app.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var viewmodelLogin: ViewModelLogin
    private lateinit var registerBinding: FragmentRegisterBinding
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
        registerBinding = FragmentRegisterBinding.inflate(layoutInflater)
        registerBinding.viewModelRegister=viewmodelLogin
        registerBinding.lifecycleOwner=viewLifecycleOwner
        return registerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodelLogin.registerSuccess.observe(viewLifecycleOwner){navigate->
            if(navigate){
                findNavController().navigateUp()
                viewmodelLogin.resetNavigation()
            }
        }
        viewmodelLogin.toastMessage.observe(viewLifecycleOwner){message->
            message.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

        }
    }


}