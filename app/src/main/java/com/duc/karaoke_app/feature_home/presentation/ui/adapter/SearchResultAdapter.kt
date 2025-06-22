package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.duc.karaoke_app.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.duc.karaoke_app.feature_home.data.SongResult
import com.duc.karaoke_app.feature_home.data.Songs
import com.duc.karaoke_app.feature_home.data.UserResult

class SearchResultAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<SearchItem> = mutableListOf()
    private var userClick : ((Int) -> Unit)? = null
    private var onItemClick: ((Songs) -> Unit)? = null

    sealed class SearchItem {
        data class UserItem(val user: UserResult) : SearchItem()
        data class SongItem(val song: SongResult) : SearchItem()
    }
    companion object{
         const val TYPE_USER = 1
         const val TYPE_SONG = 2
    }
    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userName)
        val userSlogan: TextView = view.findViewById(R.id.userSlogan)
        val userImage: ImageView = view.findViewById(R.id.userImage)

        fun bind(user: UserResult){
            userName.text = user.username
            userSlogan.text = user.slogan
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.placeholder_image)
                .into(userImage)
            itemView.setOnClickListener {
                userClick?.invoke(user.id)
                Log.e("item đã click",user.id.toString())
            }
        }
    }
    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songTitle: TextView = view.findViewById(R.id.songTitle)
        val songSubtitle: TextView = view.findViewById(R.id.songSubtitle)
        val songImage: ImageView = view.findViewById(R.id.songImage)

        fun bind(song: SongResult){
            songTitle.text = song.title
            songSubtitle.text = song.subTitle
            Glide.with(itemView.context)
                .load(song.coverImageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(songImage)
            itemView.setOnClickListener {
                onItemClick?.invoke(song)  //onItemClick?.invoke(song) là cách ngắn gọn để kiểm tra onItemClick không null và gọi lambda.
                // tương đương onItemClick?.let{it(song)}
                Log.e("Adapter", "Item clicked: ${song.title}")

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SearchItem.UserItem -> TYPE_USER
            is SearchItem.SongItem -> TYPE_SONG
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_USER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_song, parent, false)
            SongViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when(item){
            is SearchItem.UserItem -> (holder as UserViewHolder).bind(item.user)
            is SearchItem.SongItem -> (holder as SongViewHolder).bind(item.song)
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<SearchItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnUserClick(listener: ((Int) -> Unit)?) {
        userClick = listener
    }
    fun setOnItemClick(listener: ((Songs) -> Unit)?) {
        onItemClick = listener
    }

}
