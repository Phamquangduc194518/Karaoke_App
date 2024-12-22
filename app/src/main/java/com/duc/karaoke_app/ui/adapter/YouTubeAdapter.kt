package com.duc.karaoke_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.YouTubeVideoItem

class YouTubeAdapter(
    private var onVideoClick: (String) -> Unit
) :  ListAdapter<YouTubeVideoItem, YouTubeAdapter.YoutubeViewHolder>(DiffCallback()) {

    class YoutubeViewHolder(itemView: View,private val onVideoClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val thumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)

        fun bind(video: YouTubeVideoItem) {
            title.text = video.snippet.title
            Glide.with(itemView.context)
                .load(video.snippet.thumbnails.default.url)
                .into(thumbnail)

            itemView.setOnClickListener{
                onVideoClick(video.id.videoId)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return YoutubeViewHolder(view,onVideoClick)
    }


    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video)
    }

    class DiffCallback : DiffUtil.ItemCallback<YouTubeVideoItem>() {
        override fun areItemsTheSame(oldItem: YouTubeVideoItem, newItem: YouTubeVideoItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: YouTubeVideoItem, newItem: YouTubeVideoItem) = oldItem == newItem
    }

}