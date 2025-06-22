package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Songs
import com.duc.karaoke_app.feature_home.data.topSong

class TopSongAdapter() : RecyclerView.Adapter<TopSongAdapter.TopSongViewHolder>() {
    private var topSongList: List<topSong> = listOf()
    private var onTopSongClick: ((Songs) -> Unit)?= null
    class TopSongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val topSongImage: ImageView = itemView.findViewById(R.id.img_TopSongCover)
        val topSongTitle: TextView = itemView.findViewById(R.id.tv_TopSongTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_top_song, parent,false)
        return TopSongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topSongList.size
    }

    override fun onBindViewHolder(holder: TopSongViewHolder, position: Int) {
        val topSong = topSongList[position]
        Glide.with(holder.itemView.context)
            .load(topSong.song.coverImageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))// bo góc ảnh
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.topSongImage)

        holder.topSongTitle.text = topSong.song.title
        holder.itemView.setOnClickListener {
            onTopSongClick?.invoke(topSong.song)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateTopSong(topSongList: List<topSong>) {
        this.topSongList = topSongList
        notifyDataSetChanged() // Cập nhật giao diện
    }

    fun setOnTopSongClick(listener: ((Songs) -> Unit)?) {
        onTopSongClick = listener
    }
}