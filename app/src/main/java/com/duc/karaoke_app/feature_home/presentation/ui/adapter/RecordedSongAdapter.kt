package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.RecordedSong

class RecordedSongAdapter : RecyclerView.Adapter<RecordedSongAdapter.RecordedSongViewHolder>() {
    private var items: List<RecordedSong> = listOf()
    private var onStartDragging: ((Int) -> Unit)? = null
    private var statusChangeListener: OnSongStatusChangeListener? = null
    interface OnSongStatusChangeListener {
        fun onStatusChanged(songId: Int, newStatus: String)
    }

    inner class RecordedSongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSongName: TextView = itemView.findViewById(R.id.tvSongName)
        val tvUploadTime: TextView = itemView.findViewById(R.id.tvUploadTime)
        val tvLikes: TextView = itemView.findViewById(R.id.tvLikes)
        val tvComments: TextView = itemView.findViewById(R.id.tvComments)
        val switchStatus: SwitchCompat = itemView.findViewById(R.id.switchStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordedSongViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recorded_song, parent, false)
        return RecordedSongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecordedSongViewHolder, position: Int) {
        val song = items[position]
        holder.tvTitle.text = song.title
        holder.tvSongName.text = song.songName
        holder.tvUploadTime.text = song.uploadTime.substringBefore("T")
        holder.tvLikes.text = song.likesCount.toString()
        holder.tvComments.text = song.commentsCount.toString()
        holder.switchStatus.isChecked = song.status == "public"
        holder.switchStatus.text = if (song.status == "public") "Public" else "Private"

        Glide.with(holder.itemView.context)
            .load(song.coverImageUrl)
            .into(holder.imgCover)

        holder.switchStatus.setOnCheckedChangeListener { _, isChecked ->
            val newStatus = if (isChecked) "public" else "private"
            holder.switchStatus.text = newStatus.replaceFirstChar { it.uppercase() }
            statusChangeListener?.onStatusChanged(song.id, newStatus)
        }

        holder.itemView.setOnLongClickListener {
            val clipData = ClipData.newPlainText("", "")
            val shadow = View.DragShadowBuilder(it)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                it.startDragAndDrop(clipData, shadow, it, 0)
            } else {
                it.startDrag(clipData, shadow, it, 0)
            }
            onStartDragging?.invoke(song.id)
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecordedSong(newItems: List<RecordedSong>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    fun listenerOnLongClick(listener: ((Int) -> Unit)?){
        onStartDragging= listener
    }

    fun setOnStatusChangeListener(listener: OnSongStatusChangeListener) {
        this.statusChangeListener = listener
    }
}