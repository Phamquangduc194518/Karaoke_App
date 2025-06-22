package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Lyric


class ViewDuetSongAdapter() : RecyclerView.Adapter<ViewDuetSongAdapter.ViewDuetSongViewHolder>() {
    private var lyrics: List<Lyric> = listOf()
    private var currentLyricIndex =-1
    private val handler = Handler(Looper.getMainLooper()) // Handler để cập nhật giao diện

    class ViewDuetSongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lyricText: TextView = itemView.findViewById(R.id.lyricText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewDuetSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lyric, parent, false)
        return ViewDuetSongViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewDuetSongViewHolder, position: Int) {
        val lyric = lyrics[position]
        holder.lyricText.text = lyric.text

        // Đổi màu dựa theo vai trò
        holder.lyricText.setTextColor(
            when (lyric.singer) {
                "Nam" -> Color.parseColor("#4482a5")
                "Nữ" -> Color.parseColor("#ff4572")
                "Cả hai" -> Color.parseColor("#329774")
                else -> Color.WHITE
            }
        )
        if(position == currentLyricIndex){
            holder.lyricText.setTextSize(25F)
            holder.lyricText.setTextColor(Color.YELLOW) // Chữ đen cho nổi bật
        }else {
            holder.lyricText.setBackgroundColor(Color.TRANSPARENT) // Nền trong suốt cho dòng chưa hát
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLyricDuetSong(newLyric: List<Lyric>){
        lyrics= newLyric
        notifyDataSetChanged()
    }

    fun updateCurrentLyric(currentTime: Int){
        for(i in lyrics.indices){
            val lyric = lyrics[i]
            if(currentTime.toFloat() in lyric.start..lyric.end){
                if(currentLyricIndex != i){
                    currentLyricIndex=i
                    notifyDataSetChanged()
                }
                break
            }
        }
    }

    fun scrollToCurrentLyric(recyclerView: RecyclerView){
        handler.post{
            if(currentLyricIndex != -1){
                val targetIndex = if(currentLyricIndex > 1) currentLyricIndex +2 else currentLyricIndex
                recyclerView.smoothScrollToPosition(targetIndex)
            }
        }
    }

    override fun getItemCount(): Int = lyrics.size
}
