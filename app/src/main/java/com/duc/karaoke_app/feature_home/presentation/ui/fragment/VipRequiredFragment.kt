package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentVipRequiredBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome

class VipRequiredFragment : Fragment() {
    private lateinit var vipRequiredBinding: FragmentVipRequiredBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vipRequiredBinding = FragmentVipRequiredBinding.inflate(layoutInflater)
        vipRequiredBinding.viewModelVipRequired = viewModel
        vipRequiredBinding.lifecycleOwner = viewLifecycleOwner
        return vipRequiredBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isClickVipUpdateOfLiveStream.observe(viewLifecycleOwner){isClickVipUpdateOfLiveStream->
            if(isClickVipUpdateOfLiveStream){
                viewModel.resetIsClickVipUpdateOfLiveStream()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, VipUpgradeFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}