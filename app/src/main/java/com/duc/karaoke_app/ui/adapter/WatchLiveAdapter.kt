package com.duc.karaoke_app.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.data.model.CommentLiveStreamList
import com.duc.karaoke_app.utils.ConversionTime

class WatchLiveAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var comments: List<CommentLiveStreamList> = listOf()
    private var onAvatarClick: ((Int) -> Unit)? = null
    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_IMAGE = 1
        const val TYPE_STICKER = 2
    }

    override fun getItemViewType(position: Int): Int {
        val comment = comments[position]
        return when {
            // Ưu tiên sticker nếu có
            !comment.urlSticker.isNullOrBlank()-> TYPE_STICKER
            // Nếu có ảnh
            !comment.urlImage.isNullOrBlank() -> TYPE_IMAGE
            else -> TYPE_TEXT
        }
    }

    // ViewHolder cho bình luận text
    inner class TextCommentLiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvComment: TextView = itemView.findViewById(R.id.tvCommentText)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentLiveStreamList) {
            tvComment.text = comment.commentText
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.commentTime)
            tvUserName.text = comment.userCommentLive.username
            Glide.with(itemView.context)
                .load(comment.userCommentLive.avatar_url)
                .into(ivAvatar)
            ivAvatar.setOnClickListener{
                onAvatarClick?.invoke(comment.userCommentLive.user_id)
            }
            tvUserName.setOnClickListener{
                onAvatarClick?.invoke(comment.userCommentLive.user_id)
            }
        }
    }

    // ViewHolder cho bình luận có ảnh
    inner class ImageCommentLiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageComment: ImageView = itemView.findViewById(R.id.imageComment)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatarComment)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentLiveStreamList) {
            tvUserName.text = comment.userCommentLive.username
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.commentTime)
            Glide.with(itemView.context)
                .load(comment.userCommentLive.avatar_url)
                .into(ivAvatar)
            Glide.with(itemView.context)
                .load(comment.urlSticker)
                .into(imageComment)
            ivAvatar.setOnClickListener{
                onAvatarClick?.invoke(comment.userCommentLive.user_id)
            }
            tvUserName.setOnClickListener{
                onAvatarClick?.invoke(comment.userCommentLive.user_id)
            }
        }
    }

    // ViewHolder cho bình luận có sticker
    inner class StickerCommentLiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageComment: ImageView = itemView.findViewById(R.id.imageComment)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatarComment)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentLiveStreamList) {
            tvUserName.text = comment.userCommentLive.username
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.commentTime)
            Glide.with(itemView.context)
                .load(comment.userCommentLive.avatar_url)
                .into(ivAvatar)
            Glide.with(itemView.context)
                .load(comment.urlSticker)
                .into(imageComment)
            ivAvatar.setOnClickListener{
                onAvatarClick?.invoke(comment.userCommentLive.user_id)
            }
            tvUserName.setOnClickListener{
                onAvatarClick?.invoke(comment.userCommentLive.user_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_comment_sticker, parent, false)
                ImageCommentLiveViewHolder(view)
            }

            TYPE_STICKER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_comment_sticker, parent, false)
                StickerCommentLiveViewHolder(view)
            }

            else -> { // TYPE_TEXT
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.comment_item, parent, false)
                TextCommentLiveViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val comment = comments[position]
        when(holder){
            is TextCommentLiveViewHolder -> holder.bind(comment)
            is ImageCommentLiveViewHolder -> holder.bind(comment)
            is StickerCommentLiveViewHolder -> holder.bind(comment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCommentLists(newComment: List<CommentLiveStreamList>) {
        comments = newComment
        notifyDataSetChanged()
    }

    fun setOnAvatarAndNameClick(listener: ((Int) -> Unit)?) {
        onAvatarClick = listener
    }
}