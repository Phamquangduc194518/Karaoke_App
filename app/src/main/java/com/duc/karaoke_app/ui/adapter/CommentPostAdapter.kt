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
import com.duc.karaoke_app.data.model.Comment
import com.duc.karaoke_app.data.model.CommentDone
import com.duc.karaoke_app.utils.ConversionTime

class CommentPostAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var comments: List<CommentDone> = listOf()
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
            !comment.urlSticker.isNullOrBlank() -> TYPE_STICKER
            // Nếu có ảnh
            !comment.urlImage.isNullOrBlank() -> TYPE_IMAGE
            else -> TYPE_TEXT
        }
    }

    // ViewHolder cho bình luận text
    inner class TextCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvComment: TextView = itemView.findViewById(R.id.tvCommentText)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentDone) {
            tvComment.text = comment.comment_text
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.comment_time)
            tvUserName.text = comment.user.username
            Glide.with(itemView.context)
                .load(comment.user.avatar_url)
                .into(ivAvatar)
            ivAvatar.setOnClickListener{
                onAvatarClick?.invoke(comment.user_id)
            }
            tvUserName.setOnClickListener{
                onAvatarClick?.invoke(comment.user_id)
            }
        }
    }

    // ViewHolder cho bình luận có ảnh
    inner class ImageCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageComment: ImageView = itemView.findViewById(R.id.imageComment)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatarComment)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentDone) {
            tvUserName.text = comment.user.username
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.comment_time)
            Glide.with(itemView.context)
                .load(comment.user.avatar_url)
                .into(ivAvatar)
            Glide.with(itemView.context)
                .load(comment.urlSticker)
                .into(imageComment)
            ivAvatar.setOnClickListener{
                onAvatarClick?.invoke(comment.user_id)
            }
            tvUserName.setOnClickListener{
                onAvatarClick?.invoke(comment.user_id)
            }
        }
    }

    // ViewHolder cho bình luận có sticker
    inner class StickerCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageComment: ImageView = itemView.findViewById(R.id.imageComment)
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatarComment)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvTimePosted: TextView = itemView.findViewById(R.id.tvTimePosted)
        fun bind(comment: CommentDone) {
            tvUserName.text = comment.user.username
            val conversionTime = ConversionTime()
            tvTimePosted.text = conversionTime.formatRelativeTimePretty(comment.comment_time)
            Glide.with(itemView.context)
                .load(comment.user.avatar_url)
                .into(ivAvatar)
            Glide.with(itemView.context)
                .load(comment.urlSticker)
                .into(imageComment)
            ivAvatar.setOnClickListener{
                onAvatarClick?.invoke(comment.user_id)
            }
            tvUserName.setOnClickListener{
                onAvatarClick?.invoke(comment.user_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_comment_sticker, parent, false)
                ImageCommentViewHolder(view)
            }

            TYPE_STICKER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_comment_sticker, parent, false)
                StickerCommentViewHolder(view)
            }

            else -> { // TYPE_TEXT
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.comment_item, parent, false)
                TextCommentViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val comment = comments[position]
        when(holder){
            is TextCommentViewHolder -> holder.bind(comment)
            is ImageCommentViewHolder -> holder.bind(comment)
            is StickerCommentViewHolder -> holder.bind(comment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCommentLists(newComment: List<CommentDone>) {
        comments = newComment
        notifyDataSetChanged()
    }

    fun setOnAvatarAndNameClick(listener: ((Int) -> Unit)?) {
        onAvatarClick = listener
    }

}