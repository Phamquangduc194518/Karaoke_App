package com.duc.karaoke_app.core.utils

import android.text.Html
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

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
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrlCircle")
    fun loadImageCircle(view: ImageView, url: String?){
        if(!url.isNullOrEmpty()){
            Glide.with(view.context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("imageCornerRadius")
    fun loadImageCornerRadius(view: ImageView, url: String?){
        if(!url.isNullOrEmpty()){
            Glide.with(view.context)
                .load(url)
                .apply(
                    RequestOptions()
                        .transform(RoundedCorners(18)) // Bo góc với giá trị radius
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                )
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(view)
        }
    }

    fun extractYouTubeVideoId(url: String): String? {
        // Sử dụng regex để lấy video id sau "v=" trong URL
        val regex = "(?<=watch\\?v=|/videos/|embed/)[^#\\&\\?]*".toRegex()
        return regex.find(url)?.value
    }
    @JvmStatic
    @BindingAdapter("videoPlay")
    fun loadVideo(playerView:com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView, videoUrl: String?){
        if(videoUrl.isNullOrEmpty()) return

        val videoId = extractYouTubeVideoId(videoUrl)
        if(videoId.isNullOrEmpty()) return
        playerView.addYouTubePlayerListener(object: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(videoId, 0f)
            }
        })
    }



}

