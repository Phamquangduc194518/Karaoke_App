package com.duc.karaoke_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.FollowerItem

class FollowersAdapter() :
    RecyclerView.Adapter<FollowersAdapter.FollowerViewHolder>() {

    private var followers: List<FollowerItem> = listOf()
    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val txtUsername: TextView = itemView.findViewById(R.id.txtUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_followers, parent, false)
        return FollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val follower = followers[position].follower
        holder.txtUsername.text = follower.username

        Glide.with(holder.itemView.context)
            .load(follower.avatarUrl)
            .placeholder(R.drawable.placeholder_image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))// bo góc ảnh
            .into(holder.imgAvatar)
    }

    override fun getItemCount(): Int = followers.size

    fun updateFollower(mewFollowers: List<FollowerItem>){
        this.followers = mewFollowers
    }
}
