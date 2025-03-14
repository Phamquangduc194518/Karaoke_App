package com.duc.karaoke_app.ui.adapter

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
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.generated.callback.OnClickListener

class DuetSongAdapter : RecyclerView.Adapter<DuetSongAdapter.DuetSongViewHolder>() {
    private var duetSongList: List<Songs> = listOf()
    private var onItemClick: ((Songs) -> Unit)? = null
    class DuetSongViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val coverImage: ImageView = itemView.findViewById(R.id.ivCoverImage)
        val duetSongTitle: TextView = itemView.findViewById(R.id.tvDuetSongTitle)
        val duetSongSubtitle: TextView = itemView.findViewById(R.id.tvDuetSongSubtitle)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.ivFavoriteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuetSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_duet_song, parent, false)
        return DuetSongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return duetSongList.size
    }

    override fun onBindViewHolder(holder: DuetSongViewHolder, position: Int) {
        var duetSong = duetSongList[position]
        holder.duetSongTitle.text = duetSong.title
        holder.duetSongSubtitle.text = duetSong.subTitle
        Glide.with(holder.itemView.context)
            .load(duetSong.coverImageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))// bo góc ảnh
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.coverImage)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(duetSong)
            Log.e("Adapter", "Item clicked: ${duetSong.title}")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDuetSongLists(newDuetSong: List<Songs>) {
        duetSongList = newDuetSong
        notifyDataSetChanged() // Cập nhật giao diện
    }

    fun setOnItemClick(listener :((Songs)-> Unit)?){
        onItemClick = listener
    }
}