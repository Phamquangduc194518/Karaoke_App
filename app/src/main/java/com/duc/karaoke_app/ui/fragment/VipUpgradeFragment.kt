package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentVipUpgradeBinding

class VipUpgradeFragment : Fragment() {

    private val viewModel: ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private lateinit var binding: FragmentVipUpgradeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVipUpgradeBinding.inflate(layoutInflater)
        binding.viewModelVipUpgrade= viewModel
        binding.lifecycleOwner= viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkVipStatus()
        binding.purchaseButton.setOnClickListener {
            viewModel.updateVipCheck()
            viewModel.onPurchaseVipClicked(requireActivity())
        }

        viewModel.isNavigate.observe(viewLifecycleOwner){
            if(viewModel.isNavigate.value == true){
                viewModel.resetNavigate()
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsAndPrivacyFragment())
                .commit()
        }
    }

}