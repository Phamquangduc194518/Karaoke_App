package com.duc.karaoke_app.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.data.model.Topic
import com.duc.karaoke_app.data.model.Video
import com.duc.karaoke_app.R

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    private var videoList: List<Video> = listOf()

    // Biến callback để truyền sự kiện click lên Fragment
    private var onVideoClickListener: ((Video) -> Unit)? = null

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSubTitle: TextView = itemView.findViewById(R.id.tvDescription)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        val ivThumbnail: ImageView= itemView.findViewById(R.id.ivThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.tvTitle.text = video.title
        holder.tvSubTitle.text = video.subTitle
        holder.tvDuration.text = video.duration
        Glide.with(holder.itemView)
            .load(video.thumbnail)
            .into(holder.ivThumbnail)
        holder.itemView.setOnClickListener {
            onVideoClickListener?.invoke(video)
        }
    }

    // Hàm thiết lập sự kiện click cho Video
    fun setOnVideoClickListener(listener: (Video) -> Unit) {
        onVideoClickListener = listener
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateVideoList(newVideo: List<Video>) {
        videoList = newVideo
        notifyDataSetChanged()
    }

}