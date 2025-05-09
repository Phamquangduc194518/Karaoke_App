package com.duc.karaoke_app.ui.adapter.messenger

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.UserInfo

class OnlineUserAdapter: RecyclerView.Adapter<OnlineUserAdapter.OnlineUserViewHolder>() {

    private var onlineUserList: List<UserInfo> = listOf()

    inner class OnlineUserViewHolder(view: View): RecyclerView.ViewHolder(view){
          val ivAvatar: ImageView = view.findViewById(R.id.user_avatar)
          val tvBadOnline: TextView = view.findViewById(R.id.tvBadOnline)
          val tvName: TextView = view.findViewById(R.id.user_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_online_user, parent, false)
        return OnlineUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return onlineUserList.size
    }

    override fun onBindViewHolder(holder: OnlineUserViewHolder, position: Int) {
        val user = onlineUserList[position]
        holder.tvName.text = user.username
        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.ivAvatar)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateOnlineUsers(mlist :List<UserInfo>){
        onlineUserList = mlist
        notifyDataSetChanged()
    }
}