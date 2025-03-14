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
import com.duc.karaoke_app.data.model.FollowingItem

class FollowingsAdapter() : RecyclerView.Adapter<FollowingsAdapter.FollowingsViewHolder>() {

    private var following: List<FollowingItem> = listOf()
    inner class FollowingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatarFollowing)
        val txtUsername: TextView = itemView.findViewById(R.id.tvUsernameFollowing)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_followings, parent, false)
        return FollowingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowingsViewHolder, position: Int) {
        val following = following[position].following
        holder.txtUsername.text = following.username

        Glide.with(holder.itemView.context)
            .load(following.avatarUrl)
            .placeholder(R.drawable.placeholder_image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))// bo góc ảnh
            .into(holder.imgAvatar)
    }

    override fun getItemCount(): Int = following.size

    fun updateFollowing(mewFollowing: List<FollowingItem>){
        this.following = mewFollowing
    }
}
