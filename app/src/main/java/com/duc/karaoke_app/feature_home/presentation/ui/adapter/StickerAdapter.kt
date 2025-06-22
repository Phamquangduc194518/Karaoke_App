package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.duc.karaoke_app.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.feature_home.data.Sticker

class StickerAdapter() : RecyclerView.Adapter<StickerAdapter.StickerViewHolder>() {

    private var stickerList: List<Sticker> = listOf()
    private var onStickerClick: ((Sticker) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sticker, parent, false)
        return StickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        val stickerUrl = stickerList[position]
        Glide.with(holder.itemView.context)
            .load(stickerUrl.stickerUrl)
            .into(holder.imageViewSticker)
        holder.itemView.setOnClickListener {
            onStickerClick?.invoke(stickerUrl)
        }
    }

    override fun getItemCount(): Int = stickerList.size

    inner class StickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewSticker: ImageView = itemView.findViewById(R.id.imageViewSticker)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun upDateSticker(newSticker: List<Sticker>){
        stickerList= newSticker
        notifyDataSetChanged()
    }

    fun onClickSticker(listener: ((Sticker)->Unit)?){
        onStickerClick= listener
    }


}