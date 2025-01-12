package com.duc.karaoke_app.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.CommentDone

class CommentPostAdapter : RecyclerView.Adapter<CommentPostAdapter.CommentViewHolder>() {
    private var comments: List<CommentDone> = listOf()
    class CommentViewHolder(view: View): RecyclerView.ViewHolder(view){
        val avatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val commentTime: TextView = itemView.findViewById(R.id.tvTimePosted)
        val commentText: TextView = itemView.findViewById(R.id.tvCommentText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.commentText.text= comment.comment_text
        holder.commentTime.text= comment.comment_time
        holder.userName.text= comment.user.username
        Glide.with(holder.itemView.context)
            .load(comment.user.avatar_url)
            .into(holder.avatar)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCommentLists(newComment: List<CommentDone>){
        comments= newComment
        notifyDataSetChanged()
    }

}