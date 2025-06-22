package com.duc.karaoke_app.feature_chat.presentation.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_chat.data.RoomResponse
import com.duc.karaoke_app.core.utils.ConversionTime


class ChatRoomAdapter(): RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    private var chatRoomList: List<RoomResponse> = listOf()
    private var currentUserId: Int = -1
    private var onClickItemChat :((RoomResponse) -> Unit) ?= null
    inner class ChatRoomViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvLastMessage: TextView = view.findViewById(R.id.tvLastMessage)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatRoomList.size
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val room = chatRoomList[position]
        val otherUser = room.members.firstOrNull{
            it.user.userId != currentUserId
        }
        holder.tvName.text = otherUser?.user?.username ?: ""
        holder.tvLastMessage.text = room.messages[0].content
        val conversionTime = ConversionTime()
        holder.tvTime.text = conversionTime.formatRelativeTimePretty(room.messages[0].createdAt)
        if(room.messages[0].status != "read"){
            holder.itemView.setBackgroundColor(Color.parseColor("#E7F3FF"))
            holder.tvLastMessage.setTypeface(null, Typeface.BOLD)
            holder.tvLastMessage.setTextColor(Color.parseColor("#000000"))
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.tvLastMessage.setTypeface(null, Typeface.NORMAL)
            holder.tvLastMessage.setTextColor(Color.parseColor("#999999"))
        }
        Glide.with(holder.itemView.context)
            .load(otherUser?.user?.avatarUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.ivAvatar)
        holder.itemView.setOnClickListener {
            onClickItemChat?.invoke(room)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRoom(mlist : List<RoomResponse>){
        chatRoomList = mlist
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setCurrentUserId(id: Int) {
        this.currentUserId = id
        notifyDataSetChanged()
    }

    fun setOnItemChatClick(listener: ((RoomResponse) -> Unit)?) {
        onClickItemChat = listener
    }
}