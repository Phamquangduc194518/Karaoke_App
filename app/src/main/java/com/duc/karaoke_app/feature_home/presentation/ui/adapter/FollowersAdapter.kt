package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
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
import com.duc.karaoke_app.feature_home.data.FollowerItem

class FollowersAdapter() :
    RecyclerView.Adapter<FollowersAdapter.FollowerViewHolder>() {

    private var followers: List<FollowerItem> = listOf()
    private var onItemClick: ((Int) -> Unit) ?= null
    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        val txtUsername: TextView = itemView.findViewById(R.id.txtUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_followers, parent, false)
        return FollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val follower = followers[position].follower
        Log.e("dữ liệu follower",follower.username.toString())
        holder.txtUsername.text = follower.username

        Glide.with(holder.itemView.context)
            .load(follower.avatarUrl)
            .placeholder(R.drawable.placeholder_image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))// bo góc ảnh
            .into(holder.imgAvatar)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(follower.id)
            Log.e("click vào follower thứ:",follower.id.toString())
        }

    }

    override fun getItemCount(): Int = followers.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateFollower(mewFollowers: List<FollowerItem>){
        this.followers = mewFollowers
        notifyDataSetChanged()
    }

    fun setOnItemClick(listener: ((Int) -> Unit)?) {
        onItemClick = listener
    }
}
