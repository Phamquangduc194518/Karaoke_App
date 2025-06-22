package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentMyDialogBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome

class MyDialogFragment : DialogFragment() {

    private lateinit var myDialogBinding: FragmentMyDialogBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        myDialogBinding = FragmentMyDialogBinding.inflate(layoutInflater)
        myDialogBinding.viewModelDialog = viewModel
        myDialogBinding.lifecycleOwner = this

        val dialog = AlertDialog.Builder(requireContext())
            .setView(myDialogBinding.root)
            .setCancelable(false)
            .create()

        myDialogBinding.btnSend.setOnClickListener {
            dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog

    }

}