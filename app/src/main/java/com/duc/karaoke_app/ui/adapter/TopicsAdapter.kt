package com.duc.karaoke_app.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.Topic
import com.duc.karaoke_app.data.model.Video

class TopicsAdapter : RecyclerView.Adapter<TopicsAdapter.TopicsViewHolder>() {
    private var topicsList: List<Topic> = listOf()
    // Biến callback để truyền sự kiện click lên Fragment
    private var onVideoClickListener: ((Video) -> Unit)? = null
    class TopicsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = itemView.findViewById(R.id.tv_topic_name)
        val rcvVideo: RecyclerView = itemView.findViewById(R.id.rcv_video_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TopicsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topicsList.size
    }

    override fun onBindViewHolder(holder: TopicsViewHolder, position: Int) {
        val topic = topicsList[position]
        holder.title.text = topic.title
        val videoAdapter = VideosAdapter(topic.videos)
        videoAdapter.setOnVideoClick { video ->
            if (onVideoClickListener != null) {
                onVideoClickListener?.invoke(video)
            } else {
                Log.e("TopicsAdapter", "onVideoClickListener chưa được set!")
            }
        }

        holder.rcvVideo.layoutManager = GridLayoutManager(holder.itemView.context, 2)
        holder.rcvVideo.adapter = videoAdapter
    }

    // Hàm thiết lập sự kiện click cho Video
    fun setOnVideoClickListener(listener: (Video) -> Unit) {
        onVideoClickListener  = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTopicsList(newTopic: List<Topic>) {
        topicsList = newTopic
        notifyDataSetChanged() // Cập nhật giao diện
    }

}