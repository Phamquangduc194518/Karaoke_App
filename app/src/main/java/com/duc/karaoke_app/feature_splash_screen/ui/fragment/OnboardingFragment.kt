package com.duc.karaoke_app.feature_splash_screen.ui.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import com.duc.karaoke_app.R
import com.duc.karaoke_app.core.utils.PermissionUtils
import com.duc.karaoke_app.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {
    private var title: String? = null
    private var description: String? = null
    private var imageResource = 0
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private var requiredPermission: String? = null
    private val PERMISSION_REQUEST_CODE = 1001
    private var permissionCallback: OnboardingPermissionGrantedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requiredPermission = arguments?.getString(ARG_PERMISSION)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val titleRes = requireArguments().getInt(ARG_TITLE_RES)
        val descRes = requireArguments().getInt(ARG_DESC_RES)
        val animRes = requireArguments().getInt(ARG_ANIM_RES)

        binding.textOnboardingTitle.text = getString(titleRes)
        binding.textOnboardingDescription.text = getString(descRes)
        binding.imageOnboarding.setAnimation(animRes)

        binding.btnAllow.setOnClickListener {
            requiredPermission?.let {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    PermissionUtils.setPermissionGranted(requireContext(), it)
                    checkAndNotifyIfAllPermissionsGranted()
                } else {
                    requestPermissions(arrayOf(it), PERMISSION_REQUEST_CODE)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionUtils.setPermissionGranted(requireContext(), requiredPermission!!)
                Toast.makeText(context, "Quyền đã được cấp!", Toast.LENGTH_SHORT).show()
                checkAndNotifyIfAllPermissionsGranted()
            } else {
                Toast.makeText(context, "Bạn cần cấp quyền để tiếp tục", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val ARG_TITLE_RES = "titleRes"
        private const val ARG_DESC_RES = "descRes"
        private const val ARG_ANIM_RES = "animRes"
        private const val ARG_PERMISSION = "permission"

        fun newInstance(
            titleResId: Int,
            descResId: Int,
            animResId: Int,
            requiredPermission: String?
        ): OnboardingFragment {
            return OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TITLE_RES, titleResId)
                    putInt(ARG_DESC_RES, descResId)
                    putInt(ARG_ANIM_RES, animResId)
                    putString(ARG_PERMISSION, requiredPermission)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnboardingPermissionGrantedCallback) {
            permissionCallback = context
        }
    }

    private fun checkAndNotifyIfAllPermissionsGranted() {
        if (PermissionUtils.areAllRequiredPermissionsGranted(requireContext())) {
            permissionCallback?.onAllPermissionsGranted()
        }
    }
}

interface OnboardingPermissionGrantedCallback {
    fun onAllPermissionsGranted()
}
