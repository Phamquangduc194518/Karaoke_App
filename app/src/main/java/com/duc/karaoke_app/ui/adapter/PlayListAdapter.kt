package com.duc.karaoke_app.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.Songs
import com.google.android.material.shape.RoundedCornerTreatment

class PlayListAdapter() : RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>() {
    private var playlists: List<Songs> = listOf()
    private var onItemClick: ((Songs) -> Unit)? = null   //Đây là type declaration cho biến onItemClick. Biến này là một lambda function hoặc hàm ẩn danh với:
    //Tham số đầu vào là một đối tượng kiểu Songs.
    //Không trả về bất kỳ giá trị nào (Unit tương đương với void trong Java).

    private var onFavoriteClick: ((Songs) -> Unit)? = null
    private var mlist: List<Int> = listOf()

    class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImage: ImageView = itemView.findViewById(R.id.ivCoverImage)
        val playlistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTitle)
        val playlistSubtitle: TextView = itemView.findViewById(R.id.tvPlaylistSubtitle)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.ivFavoriteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_play_list, parent, false)
        return PlayListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return (playlists.size)/4
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.playlistTitle.text = playlist.title
        holder.playlistSubtitle.text = playlist.subTitle
        Glide.with(holder.itemView.context)
            .load(playlist.coverImageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.coverImage)
        // Xử lý sự kiện click
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(playlist)  //onItemClick?.invoke(song) là cách ngắn gọn để kiểm tra onItemClick không null và gọi lambda.
            // tương đương onItemClick?.let{it(song)}
            Log.e("Adapter", "Item clicked: ${playlist.title}")

        }
        if (mlist.contains(playlist.id)) {
            holder.favoriteIcon.setColorFilter(Color.parseColor("#FF0000"))
        } else {
            holder.favoriteIcon.setColorFilter(Color.parseColor("#666666"))
        }
        holder.favoriteIcon.setOnClickListener {
            onFavoriteClick?.invoke(playlist)
            if (mlist.contains(playlist.id)) {

                holder.favoriteIcon.setColorFilter(Color.parseColor("#666666"))
            } else {
                holder.favoriteIcon.setColorFilter(Color.parseColor("#FF0000"))
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(newPlaylists: List<Songs>) {
        playlists = newPlaylists
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