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
import com.duc.karaoke_app.feature_home.data.FavoriteSong
import com.duc.karaoke_app.feature_home.data.Songs


class LikeSongListAdapter : RecyclerView.Adapter<LikeSongListAdapter.LikeSongListViewHolder>() {
    private var likeSongList : List<FavoriteSong> = listOf()
    private var onItemClick: ((Songs) -> Unit)? = null
    class LikeSongListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImage: ImageView = itemView.findViewById(R.id.ivCoverImage)
        val playlistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
        val playlistSubtitle: TextView = itemView.findViewById(R.id.tvPlaylistSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeSongListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_like_song_list, parent,false)
        return LikeSongListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return likeSongList.size
    }

    override fun onBindViewHolder(holder: LikeSongListViewHolder, position: Int) {
        val song = likeSongList[position]
        holder.playlistTitle.text = song.song.title
        holder.playlistSubtitle.text = song.song.subTitle
        Glide.with(holder.itemView.context)
            .load(song.song.coverImageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))// bo góc ảnh
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.coverImage)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(song.song)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLikeSongLists(newSong: List<FavoriteSong>) {
        likeSongList = newSong
        notifyDataSetChanged() // Cập nhật giao diện
    }

    fun setOnItemClick(listener: ((Songs) -> Unit)?) {
        onItemClick = listener
    }
}