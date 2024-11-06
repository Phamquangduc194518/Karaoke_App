package com.duc.karaoke_app.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.duc.karaoke_app.MainActivity
import com.duc.karaoke_app.databinding.FragmentLoginBinding

import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.LoginRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        val repository = LoginRepository()
        val viewModelFactory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sigInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account.let { loginViewModel.signInWithGoogle(it.idToken!!) }
                } catch (e: ApiException) {
                    Log.w("Google SignIn", "Google sign in failed", e)
                }

            }

        fun signInWithGoogle() {
            val signInIntent = googleSignInClient.signInIntent
            sigInLauncher.launch(signInIntent)
        }

        binding.tvSigUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_Fragment_to_registerFragment)
        }

        binding.btnSihnInWithGoogle.setOnClickListener {
            signInWithGoogle()
        }

        loginViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            } else {
                null
            }
        }


    }
}