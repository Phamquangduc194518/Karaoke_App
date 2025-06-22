package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.CommentVideoDone
import com.duc.karaoke_app.core.utils.ConversionTime

class CommentVideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var comments: List<CommentVideoDone> = listOf()
    private var onAvatarClick: ((Int) -> Unit)? = null
    companion object {
        const val TYPE_TEXT_VIDEO = 0
        const val TYPE_IMAGE_VIDEO = 1
        const val TYPE_STICKER_VIDEO = 2
    }

    override fun getItemViewType(position: Int): Int {
        val comment = comments[position]
        return when {
            // Ưu tiên sticker nếu có
            !comment.urlSticker.isNullOrBlank() -> TYPE_STICKER_VIDEO
            // Nếu có ảnh
            !comment.urlImage.isNullOrBlank() -> TYPE_IMAGE_VIDEO
            else -> TYPE_TEXT_VIDEO
        }
    }

    inner class TextCommentVideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvComment: TextView = itemView.findViewById(R.id.tvCommentText)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentVideoDone) {
            tvComment.text = comment.commentText
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.commentTime)
            tvUserName.text = comment.user.username
            Glide.with(itemView.context)
                .load(comment.user.avatar_url)
                .into(ivAvatar)
            ivAvatar.setOnClickListener {
                onAvatarClick?.invoke(comment.userId)
            }
            tvUserName.setOnClickListener {
                onAvatarClick?.invoke(comment.userId)
            }
        }
    }

    inner class ImageCommentVideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageComment: ImageView = itemView.findViewById(R.id.imageComment)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatarComment)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentVideoDone) {
            tvUserName.text = comment.user.username
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.commentTime)
            Glide.with(itemView.context)
                .load(comment.user.avatar_url)
                .into(ivAvatar)
            Glide.with(itemView.context)
                .load(comment.urlSticker)
                .into(imageComment)
            ivAvatar.setOnClickListener {
                onAvatarClick?.invoke(comment.userId)
            }
            tvUserName.setOnClickListener {
                onAvatarClick?.invoke(comment.userId)
            }
        }
    }

    inner class StickerCommentVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageComment: ImageView = itemView.findViewById(R.id.imageComment)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatarComment)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentVideoDone) {
            tvUserName.text = comment.user.username
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.commentTime)
            Glide.with(itemView.context)
                .load(comment.user.avatar_url)
                .into(ivAvatar)
            Glide.with(itemView.context)
                .load(comment.urlSticker)
                .into(imageComment)
            ivAvatar.setOnClickListener {
                onAvatarClick?.invoke(comment.userId)
            }
            tvUserName.setOnClickListener {
                onAvatarClick?.invoke(comment.userId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_IMAGE_VIDEO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_comment_sticker, parent, false)
                ImageCommentVideoViewHolder(view)
            }

            TYPE_STICKER_VIDEO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_comment_sticker, parent, false)
                StickerCommentVideoViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.comment_item, parent, false)
                TextCommentVideoViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val comment = comments[position]
        when (holder) {
            is TextCommentVideoViewHolder -> holder.bind(comment)
            is ImageCommentVideoViewHolder -> holder.bind(comment)
            is StickerCommentVideoViewHolder -> holder.bind(comment)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCommentLists(newComment: List<CommentVideoDone>) {
        comments = newComment
        notifyDataSetChanged()
    }

    fun setOnAvatarAndNameClick(listener: ((Int) -> Unit)?) {
        onAvatarClick = listener
    }
}