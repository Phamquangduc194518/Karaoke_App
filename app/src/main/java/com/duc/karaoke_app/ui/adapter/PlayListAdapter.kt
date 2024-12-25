package com.duc.karaoke_app.ui.adapter

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.Songs
import com.google.android.material.shape.RoundedCornerTreatment

class PlayListAdapter( private val playlists: List<Songs>): RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>() {
    class PlayListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val coverImage: ImageView = itemView.findViewById(R.id.ivCoverImage)
        val playlistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
        val playlistSubtitle: TextView = itemView.findViewById(R.id.tvPlaylistSubtitle)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.ivFavoriteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_play_list, parent,false)
        return PlayListViewHolder(view)
    }

    override fun getItemCount(): Int {
       return playlists.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.playlistTitle.text = playlist.title
        holder.playlistSubtitle.text = playlist.subTitle
        Glide.with(holder.itemView.context)
            .load(playlist.coverImageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))// bo góc ảnh
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.coverImage)
    }
}