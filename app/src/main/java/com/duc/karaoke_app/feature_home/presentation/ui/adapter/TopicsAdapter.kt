package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Topic

class TopicsAdapter : RecyclerView.Adapter<TopicsAdapter.TopicsViewHolder>() {
    private var topicsList: List<Topic> = listOf()
    private var onTopicClickListener: ((Int) -> Unit)? = null
    class TopicsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = itemView.findViewById(R.id.featured_title)
        val lessonCount: TextView = itemView.findViewById(R.id.lesson_count)
        val lessonDuration: TextView = itemView.findViewById(R.id.lesson_duration)
        val featuredCategory: TextView = itemView.findViewById(R.id.featured_category)
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
        holder.lessonCount.text =topic.videos.size.toString()
        holder.lessonDuration.text =topic.duration
        holder.featuredCategory.text =topic.type
        holder.itemView.setOnClickListener {
            onTopicClickListener?.invoke(topic.id)
            Log.e("TopicAdapter", topic.id.toString())
        }
    }

    fun setOnTopicClickListener(listener: (Int) -> Unit) {
        onTopicClickListener  = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTopicsList(newTopic: List<Topic>) {
        topicsList = newTopic
        notifyDataSetChanged()
    }

}