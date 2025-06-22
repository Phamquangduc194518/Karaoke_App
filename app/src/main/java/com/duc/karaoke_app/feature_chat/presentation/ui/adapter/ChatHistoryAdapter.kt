package com.duc.karaoke_app.feature_chat.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.duc.karaoke_app.feature_chat.data.Message
import com.duc.karaoke_app.R
import com.duc.karaoke_app.core.utils.ConversionTime

class ChatHistoryAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentUserId: Int = -1
    private var messages: List<Message> = listOf()
    private var onMessageClick: ((Message) -> Unit)? = null
    companion object{
        private const val TYPE_SENT = 1
        private const val TYPE_RECEIVED = 2
    }



    override fun getItemViewType(position: Int): Int {
        return if(messages[position].senderId == currentUserId){
            TYPE_SENT
        }else{
            TYPE_RECEIVED
        }
    }

    inner class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessageSent)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatarSent)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTimeSent)

        fun bind(msg: Message){
            tvMessage.text = msg.content
            val conversionTime = ConversionTime()
            tvTime.text = conversionTime.formatRelativeTimePretty(msg.createdAt)
            Glide.with(itemView.context)
                .load(msg.sender?.avatarUrl)
                .placeholder(R.drawable.placeholder_image)
                .circleCrop()
                .into(ivAvatar)
            itemView.setOnClickListener {
                onMessageClick?.invoke(msg)
            }
        }
    }

    inner class ReceivedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessageReceived)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatarReceived)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTimeReceived)

        fun bind(msg: Message){
            tvMessage.text = msg.content
            val conversionTime = ConversionTime()
            tvTime.text = conversionTime.formatRelativeTimePretty(msg.createdAt)
            Glide.with(itemView.context)
                .load(msg.sender?.avatarUrl)
                .placeholder(R.drawable.placeholder_image)
                .circleCrop()
                .into(ivAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if(viewType == TYPE_SENT){
            val view = inflater.inflate(R.layout.item_message_sent, parent, false)
            SentViewHolder(view)
        }else{
            val view = inflater.inflate(R.layout.item_message_received, parent, false)
            ReceivedViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = messages[position]
        if (holder is SentViewHolder) {
            holder.bind(msg = msg)
        } else if (holder is ReceivedViewHolder) {
            holder.bind(msg = msg)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChatHistory(mlist:List<Message>){
        messages= mlist
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCurrentUserId(id: Int) {
        this.currentUserId = id
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(msg: Message) {
        messages = messages + msg
        notifyItemInserted(messages.size - 1)
        notifyDataSetChanged()
    }

    fun setOnClickItemView(listener: ((Message) -> Unit)?){
        onMessageClick =  listener
    }


}