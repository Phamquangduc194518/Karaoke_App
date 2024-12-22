package com.duc.karaoke_app.ui.adapter

import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object BindingAdapter {

    // BindingAdapter: Gán giá trị từ ViewModel vào Spinner
    @BindingAdapter("selectedValue")
    @JvmStatic
    fun Spinner.setSelectedValue(value: String){
        val adapter = this.adapter ?: return
        for(i in 0 until adapter.count){
            if(adapter.getItem(i).toString() == value){
                this.setSelectedValue(i.toString())
                break
            }
        }
    }

    // InverseBindingAdapter: Lấy giá trị từ Spinner về ViewModel
    @InverseBindingAdapter(attribute = "selectedValue")
    @JvmStatic
    fun Spinner.getSelectedValue(): String? {
        return selectedItem?.toString()
    }

    // Listener: Thông báo khi Spinner thay đổi giá trị
    @BindingAdapter("selectedValueAttrChanged")
    @JvmStatic
    fun Spinner.setSpinnerListeners(listener: InverseBindingListener?) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                listener?.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}