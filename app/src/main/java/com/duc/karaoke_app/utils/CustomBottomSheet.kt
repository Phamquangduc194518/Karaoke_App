package com.duc.karaoke_app.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duc.karaoke_app.R
import com.duc.karaoke_app.databinding.LayoutBottomSheetBinding
import com.duc.karaoke_app.ui.fragment.EditProfileFragment
import com.duc.karaoke_app.ui.fragment.SettingsAndPrivacyFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= LayoutBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingAndPrivacy.setOnClickListener {
            val fragment = SettingsAndPrivacyFragment()
            val transaction= parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
            dismiss()
        }
    }
}