package com.duc.karaoke_app.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.Songs


class AllSongsAdapter : RecyclerView.Adapter<AllSongsAdapter.AllSongsViewHolder>() {
    private var allSongList: List<Songs> = listOf()
    private var onItemClick: ((Songs) -> Unit)? = null
    private var onFavoriteClick: ((Songs) -> Unit)? = null
    private var mlist: List<Int> = listOf()

    inner class AllSongsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImage: ImageView = itemView.findViewById(R.id.ivCoverImage)
        val songTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
        val songSubtitle: TextView = itemView.findViewById(R.id.tvPlaylistSubtitle)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.ivFavoriteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllSongsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_play_list, parent, false)
        return AllSongsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allSongList.size
    }

    override fun onBindViewHolder(holder: AllSongsViewHolder, position: Int) {
        val song = allSongList[position]
        holder.songTitle.text = song.title
        holder.songSubtitle.text = song.subTitle
        Glide.with(holder.itemView.context)
            .load(song.coverImageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.coverImage)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(song)

        }
        if (mlist.contains(song.id)) {
            holder.favoriteIcon.setColorFilter(Color.parseColor("#FF0000"))
        } else {
            holder.favoriteIcon.setColorFilter(Color.parseColor("#666666"))
        }
        holder.favoriteIcon.setOnClickListener {
            onFavoriteClick?.invoke(song)
            if (mlist.contains(song.id)) {

                holder.favoriteIcon.setColorFilter(Color.parseColor("#666666"))
            } else {
                holder.favoriteIcon.setColorFilter(Color.parseColor("#FF0000"))
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateAllSongsLists(newSong: List<Songs>) {
        allSongList = newSong
        notifyDataSetChanged() // Cập nhật giao diện
    }

    fun setOnItemClick(listener: ((Songs) -> Unit)?) {
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