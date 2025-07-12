package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Songs

class DuetSongAdapter : RecyclerView.Adapter<DuetSongAdapter.DuetSongViewHolder>() {
    private var duetSongList: List<Songs> = listOf()
    private var onItemClick: ((Songs) -> Unit)? = null
    private var onFavoriteClick: ((Songs) -> Unit)?= null
    private var mlist: List<Int> = listOf()
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
            .transform(CenterCrop(), RoundedCorners(26))
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.coverImage)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(duetSong)
            Log.e("Adapter", "Item clicked: ${duetSong.title}")
        }
        if (mlist.contains(duetSong.id)) {
            holder.favoriteIcon.setColorFilter(Color.parseColor("#FF0000"))
        } else {
            holder.favoriteIcon.setColorFilter(Color.parseColor("#666666"))
        }
        holder.favoriteIcon.setOnClickListener{
            onFavoriteClick?.invoke(duetSong)
            if (mlist.contains(duetSong.id)) {

                holder.favoriteIcon.setColorFilter(Color.parseColor("#666666"))
            } else {
                holder.favoriteIcon.setColorFilter(Color.parseColor("#FF0000"))
            }
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
    fun setFavoriteClick(favoriteListener: ((Songs) -> Unit)?) {
        onFavoriteClick = favoriteListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavorited(newList: List<Int>) {
        mlist = newList
        notifyDataSetChanged()
    }
}