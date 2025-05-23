package com.duc.karaoke_app.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.musicPlayer.MusicPlayerViewModel
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentAudioPreviewBinding
import com.duc.karaoke_app.utils.GoogleSignInHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class AudioPreviewFragment : Fragment() {

    private lateinit var audioPreviewBinding: FragmentAudioPreviewBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        audioPreviewBinding = FragmentAudioPreviewBinding.inflate(layoutInflater)
        audioPreviewBinding.audioPreViewModel = viewModel
        audioPreviewBinding.lifecycleOwner = viewLifecycleOwner
        return audioPreviewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GoogleSignInHelper.init(requireActivity())
        viewModel.initNewExoPlayer()
        audioPreviewBinding.btnNext.setOnClickListener {
            val fragment = PostAudioFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_music_player, fragment).apply {
                commit()
            }
        }
        val signInIntent = GoogleSignInHelper.getGoogleSignInClient().signInIntent
        signInLauncher.launch(signInIntent)

        audioPreviewBinding.ivClose.setOnClickListener {
            activity?.finish()
        }

        val account = GoogleSignInHelper.getSignedInAccount()
        Log.e("Đăng nhập google","${account?.email}")

        viewModel.checkPostingCondition.observe(viewLifecycleOwner){ checkPostingCondition->
            if(checkPostingCondition != true){
                Log.e("checkPostingCondition",checkPostingCondition.toString())
                        val dialog = MyDialogFragment()
                        dialog.show(requireActivity().supportFragmentManager, "MyDialogFragmentTag")
            }
        }
    }


    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.e("GoogleSignIn", "Đăng nhập thành công: ${account.email}")
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "Sign-in failed", e)
                }
            }
        }


}


