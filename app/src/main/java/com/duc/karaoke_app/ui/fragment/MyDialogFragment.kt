package com.duc.karaoke_app.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentAlbumDetailsBinding
import com.duc.karaoke_app.databinding.FragmentMyDialogBinding

class MyDialogFragment : DialogFragment() {

    private lateinit var myDialogBinding: FragmentMyDialogBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        myDialogBinding = FragmentMyDialogBinding.inflate(layoutInflater)
        myDialogBinding.viewModelDialog = viewModel
        myDialogBinding.lifecycleOwner = this

        return AlertDialog.Builder(requireContext())
            .setView(myDialogBinding.root)
            .setCancelable(false)
            .create()
    }


}