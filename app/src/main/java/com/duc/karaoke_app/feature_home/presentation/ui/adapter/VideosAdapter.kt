package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.feature_home.data.Video
import com.duc.karaoke_app.R

class VideosAdapter(private val videos: List<Video>) : RecyclerView.Adapter<VideosAdapter.VideosViewHolder>() {
    private var onClickVideo:((Video)->Unit) ?= null
    class VideosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvVideoTitle: TextView = view.findViewById(R.id.tvTitle)
        val imgThumbnail: ImageView = view.findViewById(R.id.ivThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideosViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        val video = videos[position]
        holder.tvVideoTitle.text = video.title
        Glide.with(holder.itemView.context).load(video.thumbnail).into(holder.imgThumbnail)
        holder.itemView.setOnClickListener {
            if (onClickVideo != null) {
                onClickVideo?.invoke(video)
            } else {
                Log.e("VideosAdapter", "onClickVideo chưa được set!")
            }
        }
    }

    override fun getItemCount(): Int = videos.size

    fun setOnVideoClick(listener: ((Video) -> Unit)?) {
        onClickVideo = listener
    }
}
