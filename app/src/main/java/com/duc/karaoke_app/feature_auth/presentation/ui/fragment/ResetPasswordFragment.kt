package com.duc.karaoke_app.feature_auth.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentResetPasswordBinding
import com.duc.karaoke_app.feature_auth.data.AuthRepository
import com.duc.karaoke_app.feature_auth.presentation.viewmodel.ViewModelFactoryLogin
import com.duc.karaoke_app.feature_auth.presentation.viewmodel.ViewModelLogin

class ResetPasswordFragment : Fragment() {
    private lateinit var resetPasswordBinding: FragmentResetPasswordBinding
    private val viewmodelLogin: ViewModelLogin by activityViewModels {
        ViewModelFactoryLogin(AuthRepository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = FragmentResetPasswordBinding.inflate(layoutInflater)
        resetPasswordBinding.viewModelLogin2= viewmodelLogin
        resetPasswordBinding.lifecycleOwner= viewLifecycleOwner
        return resetPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodelLogin.forGotPassWordSuccess.observe(viewLifecycleOwner){ isClick->
            if(isClick){
                findNavController().navigate(R.id.action_resetPasswordInfo_to_login_Fragment)
            }
        }
        viewmodelLogin.toastMessage.observe(viewLifecycleOwner){message->
            message.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

        }
    }


}