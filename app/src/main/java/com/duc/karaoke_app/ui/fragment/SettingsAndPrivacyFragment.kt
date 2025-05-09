package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.LoginActivity
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentSettingsAndPrivacyBinding

class SettingsAndPrivacyFragment : Fragment() {

    private val viewModel: ViewModelHome by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private lateinit var binding: FragmentSettingsAndPrivacyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsAndPrivacyBinding.inflate(layoutInflater)
        binding.viewModelLoginSettingAndPrivacy= viewModel
        binding.lifecycleOwner= viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.logoutSuccess.observe(viewLifecycleOwner){ navigation->
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner){messege->
            messege.let{
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        // Quan sát sự kiện mua VIP thành công từ BillingViewModel
        viewModel.purchaseSuccess.observe(viewLifecycleOwner){ isVip ->
            if (isVip) {
                Toast.makeText(requireContext(), "Bạn đã trở thành VIP!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isClickVipUpgrade.observe(viewLifecycleOwner){ isCheck->
            if(isCheck){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, VipUpgradeFragment())
                    .addToBackStack(null)
                    .commit()
            }

        }

        viewModel.isNavigate.observe(viewLifecycleOwner){
            if(viewModel.isNavigate.value == true){
                viewModel.resetNavigate()
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .commit()
        }

        viewModel.isClickButtonQA.observe(viewLifecycleOwner){
            if(viewModel.isClickButtonQA.value == true){
                viewModel.resetClickButtonQA()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SongRequestFragment())
                    .commit()
            }
        }


    }

}