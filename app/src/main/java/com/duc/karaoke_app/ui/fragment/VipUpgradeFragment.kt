package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentSettingsAndPrivacyBinding
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

        viewModel.isVipResponse.observe(viewLifecycleOwner){isVipResponse->
            Log.e("Đã đăng ký chưa",isVipResponse.toString())
            if(isVipResponse != true){
                val dialog = MyDialogFragment()
                dialog.show(requireActivity().supportFragmentManager, "MyDialogFragmentTag")
            }
        }
    }

    private fun observeViewModel() {
        // Quan sát kết quả mua VIP
        viewModel.purchaseSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(
                    requireContext(),
                    "Chúc mừng! Bạn đã trở thành thành viên VIP",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Quan sát thông báo lỗi
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }


    }



}