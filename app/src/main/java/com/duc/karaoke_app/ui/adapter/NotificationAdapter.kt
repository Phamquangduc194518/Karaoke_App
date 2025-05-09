package com.duc.karaoke_app.ui.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.NotificationUser
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.utils.ConversionTime

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notifications: MutableList<NotificationUser> = mutableListOf()
    private var onItemClick: ((Int) -> Unit)? = null
    val conversionTime = ConversionTime()
    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivNotificationIcon)
        val tvSenderId: TextView = itemView.findViewById(R.id.tv_sender_id)
        val tvMessage: TextView = itemView.findViewById(R.id.tv_notificationMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvNotificationTime)
        val ctl_item_notification: ConstraintLayout = itemView.findViewById(R.id.ctl_item_notification)

        fun bind(notification: NotificationUser) {
            Glide.with(itemView.context)
                .load(notification.user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.placeholder_image)
                .into(ivIcon)
            tvSenderId.text = notification.user.username
            tvMessage.text = notification.message
            tvTime.text = conversionTime.formatRelativeTimePretty(notification.createdAt)
            if(!notification.isRead){
                ctl_item_notification.setBackgroundColor(Color.parseColor("#E7F3FF")) // Màu nổi bật (ví dụ: đỏ)
            }else {
                ctl_item_notification.setBackgroundColor(Color.parseColor("#FFFFFF")) // Màu nền trắng hoặc màu khác theo thiết kế
            }
            itemView.setOnClickListener {
                onItemClick?.invoke(notification.id)
                Log.e("chọn notification", notification.id.toString())
                if(!notification.isRead){
                    notification.isRead = true
                    notifyItemChanged(adapterPosition)
                }
            }
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

    fun setOnItemClick(listener: ((Int) -> Unit)?) {
        onItemClick = listener
    }
}
