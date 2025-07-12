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
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Songs

class RecommendedSongAdapter : RecyclerView.Adapter<RecommendedSongAdapter.RecommendedSongViewHolder>() {
    private var recommendedSongList: List<Songs> = listOf()
    private var onRecommendedSongClick: ((Songs) -> Unit)? = null

    class RecommendedSongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songImage: ImageView = itemView.findViewById(R.id.ivCoverImage)
        val songTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
        val songSubtitle: TextView = itemView.findViewById(R.id.tvPlaylistSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_like_song_list, parent, false)
        return RecommendedSongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recommendedSongList.size
    }

    override fun onBindViewHolder(holder: RecommendedSongViewHolder, position: Int) {
        val song = recommendedSongList[position]
        Glide.with(holder.itemView.context)
            .load(song.coverImageUrl)
            .transform(CenterCrop(), RoundedCorners(36))
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.songImage)

        holder.songTitle.text = song.title
        holder.songSubtitle.text = song.subTitle
        holder.itemView.setOnClickListener {
            onRecommendedSongClick?.invoke(song)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecommendedSongs(songs: List<Songs>) {
        this.recommendedSongList = songs
        notifyDataSetChanged()
    }

    fun setOnRecommendedSongClick(listener: ((Songs) -> Unit)?) {
        onRecommendedSongClick = listener
    }
}