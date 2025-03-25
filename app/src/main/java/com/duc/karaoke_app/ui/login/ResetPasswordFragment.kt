package com.duc.karaoke_app.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin
import com.duc.karaoke_app.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {
    private lateinit var resetPasswordBinding: FragmentResetPasswordBinding
    private val viewmodelLogin: ViewModelLogin by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
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