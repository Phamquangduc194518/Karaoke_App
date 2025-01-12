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
import com.duc.karaoke_app.data.model.Post
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.viewmodel.ViewModelLogin

class NewsFeedAdapter(private val viewModel: ViewModelLogin) : RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder>() {
    private var postLists: List<Post> = listOf()

    class NewsFeedViewHolder(view: View): RecyclerView.ViewHolder(view){
         val avatar: ImageView = itemView.findViewById(R.id.ivAvatarPost)
         val userName: TextView = itemView.findViewById(R.id.tvUserNamePost)
         val postTime: TextView = itemView.findViewById(R.id.tvTimePosted)
         val postContent: TextView = itemView.findViewById(R.id.tvContent)
         val postImage: ImageView = itemView.findViewById(R.id.ivPostImage)
         val likeCount: TextView = itemView.findViewById(R.id.tvLikes)
         val commentCount: TextView = itemView.findViewById(R.id.tvComments)
         val btnLike: ImageView = itemView.findViewById(R.id.btnLike)
         val btnComment: ImageView = itemView.findViewById(R.id.btnComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false )
        return NewsFeedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postLists.size
    }

    override fun onBindViewHolder(holder: NewsFeedViewHolder, position: Int) {
        val post = postLists[position]
        holder.userName.text= post.user.username
        holder.postContent.text= post.title
        holder.postTime.text=post.time
        holder.likeCount.text= post.likes_count.toString()
        holder.commentCount.text= post.comments_count.toString()
        holder.btnComment.setOnClickListener{
            viewModel.onCommentClicked(post)
        }
        Glide.with(holder.itemView.context)
            .load(post.user.avatar_url)
            .placeholder(R.drawable.user)
            .into(holder.avatar)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecordedSonglists(newPostLists: List<Post>) {
        postLists = newPostLists
        notifyDataSetChanged()
    }

}