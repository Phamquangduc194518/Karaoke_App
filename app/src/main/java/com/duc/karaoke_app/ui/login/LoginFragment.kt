package com.duc.karaoke_app.ui.login


import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import androidx.navigation.fragment.findNavController
import com.duc.karaoke_app.MainActivity
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentLoginBinding
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelLogin
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding?=null
    val binding get()=_binding!!
    private val viewmodelLogin: ViewModelLogin by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModelLogin= viewmodelLogin
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodelLogin.navigateToRegister.observe(viewLifecycleOwner){ navigate->
            if(navigate){
                Log.d("LoginFragment", "Navigating to RegisterFragment")
                findNavController().navigate(R.id.action_login_Fragment_to_registerFragment)
            }
        }
        viewmodelLogin.navigateToResetPassword.observe(viewLifecycleOwner){ navigate->
            if(navigate){
                findNavController().navigate(R.id.action_login_Fragment_to_resetPasswordInfo)
            }
        }

        viewmodelLogin.loginSuccess.observe(viewLifecycleOwner){ navigate->
            navigate?.let {
                if (it) {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
        viewmodelLogin.toastMessage.observe(viewLifecycleOwner){message->
            message.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}