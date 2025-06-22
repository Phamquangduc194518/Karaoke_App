package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Songs

class AlbumTrackListAdapter :
    RecyclerView.Adapter<AlbumTrackListAdapter.AlbumTrackListViewHolder>() {
    private var albumTrackList: List<Songs> = listOf()
    private var onItemClick: ((Songs) -> Unit)? = null
    inner class AlbumTrackListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImage: ImageView = itemView.findViewById(R.id.ivCoverImage)
        val playlistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
        val playlistSubtitle: TextView = itemView.findViewById(R.id.tvPlaylistSubtitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumTrackListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_like_song_list, parent, false)
        return AlbumTrackListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albumTrackList.size
    }

    override fun onBindViewHolder(holder: AlbumTrackListViewHolder, position: Int) {
        val song = albumTrackList[position]
        holder.playlistTitle.text = song.title
        holder.playlistSubtitle.text = song.subTitle
        Glide.with(holder.itemView.context)
            .load(song.coverImageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.coverImage)
        // Xử lý sự kiện click
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(song)  //onItemClick?.invoke(song) là cách ngắn gọn để kiểm tra onItemClick không null và gọi lambda.
            // tương đương onItemClick?.let{it(song)}
            Log.e("Adapter", "Item clicked: ${song.title}")

        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateAlbumTrackLists(newAlbumTrackLists: List<Songs>) {
        albumTrackList = newAlbumTrackLists
        notifyDataSetChanged() // Cập nhật giao diện
    }

    fun setOnAlbumTrackClick(listener: ((Songs) -> Unit)?) {
        onItemClick = listener
    }

}