package com.duc.karaoke_app.ui.adapter

import android.text.Html
import android.util.Log
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.Songs

object BindingAdapter {

    // BindingAdapter: Gán giá trị từ ViewModel vào Spinner
    @BindingAdapter("selectedValue")
    @JvmStatic
    fun Spinner.setSelectedValue(value: String) {
        val adapter = this.adapter ?: return
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == value) {
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

    @JvmStatic
    @BindingAdapter("htmlText")
    fun setHtmlText(textView: TextView, html: String?) {
        html?.let {
            textView.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
        }
    }
    @JvmStatic
    @BindingAdapter("app:tintColor")
    fun setTintColor(imageButton: ImageButton, isPlaying: Boolean) {
        val color = if (isPlaying) R.color.red else R.color.white
        imageButton.setColorFilter(ContextCompat.getColor(imageButton.context, color))
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?){
        if(!url.isNullOrEmpty()){
            Glide.with(view.context)
                .load(url)
                .transform(CircleCrop())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(view)
        }
    }


}

