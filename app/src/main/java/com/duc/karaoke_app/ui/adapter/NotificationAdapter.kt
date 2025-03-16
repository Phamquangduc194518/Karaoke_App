package com.duc.karaoke_app.ui.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.NotificationUser
import com.duc.karaoke_app.utils.ConversionTime

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notifications: MutableList<NotificationUser> = mutableListOf()
    val conversionTime = ConversionTime()
    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivNotificationIcon)
        val tvSenderId: TextView = itemView.findViewById(R.id.tv_sender_id)
        val tvMessage: TextView = itemView.findViewById(R.id.tv_notificationMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvNotificationTime)

        fun bind(notification: NotificationUser) {
            Glide.with(itemView.context)
                .load(notification.user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.placeholder_image)
                .into(ivIcon)
            tvSenderId.text = notification.user.username
            tvTime.text = conversionTime.formatRelativeTimePretty(notification.createdAt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = notifications.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataNotification(newList: List<NotificationUser>) {
        notifications.clear()
        notifications.addAll(newList)
        notifyDataSetChanged()
    }
}
