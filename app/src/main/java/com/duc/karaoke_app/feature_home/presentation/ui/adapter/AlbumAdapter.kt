package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.duc.karaoke_app.feature_home.data.Albums
import com.duc.karaoke_app.R

class AlbumAdapter() : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    private var albums: List<Albums> = listOf()
    private var setOnAlbumClick: ((Albums) -> Unit) ?= null
    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAlbumCover: ImageView = itemView.findViewById(R.id.ivAlbumCover)
        val tvAlbumTitle: TextView = itemView.findViewById(R.id.tvAlbumTitle)
        val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]

        // Gán dữ liệu
        holder.tvAlbumTitle.text = album.title
        holder.tvArtistName.text = album.artist.name

        // Load ảnh album từ URL hoặc drawable
        Glide.with(holder.itemView.context)
            .load(album.coverUrl)
            .transform(CenterCrop(),RoundedCorners(26))
            .placeholder(R.drawable.rounded_background)
            .into(holder.ivAlbumCover)

//         Xử lý sự kiện click
        holder.itemView.setOnClickListener {
            setOnAlbumClick?.invoke(album)}
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAlbums(newAlbum: List<Albums>){
        this.albums = newAlbum
        notifyDataSetChanged()
    }

    fun setOnAlbumClick(listener: ((Albums) -> Unit)?){
        setOnAlbumClick = listener
    }
}
