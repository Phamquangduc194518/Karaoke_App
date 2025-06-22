package com.duc.karaoke_app.feature_home.presentation.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Post
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome
import com.duc.karaoke_app.core.utils.ConversionTime

class NewsFeedAdapter(private val viewModel: ViewModelHome) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var postLists: List<Post> = listOf()
    private var onAvatarClick: ((Int) -> Unit)? = null
    private var onFavoriteClick: ((Int) -> Unit)? = null
    val conversionTime = ConversionTime()
    private var mlist: List<Int> = listOf()
    private var isLoading = true


    companion object {
        const val TYPE_SHIMMER = 0
        const val TYPE_READ_DATA= 1
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("Adapter", "getItemViewType: isLoading=$isLoading")
        return if(isLoading) TYPE_SHIMMER else TYPE_READ_DATA
    }

    inner class NewsFeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = itemView.findViewById(R.id.ivAvatarPost)
        val userName: TextView = itemView.findViewById(R.id.tvUserNamePost)
        val postTime: TextView = itemView.findViewById(R.id.tvTimePosted)
        val postContent: TextView = itemView.findViewById(R.id.tvContent)
        val postImage: ImageView = itemView.findViewById(R.id.ivPostImage)
        val likeCount: TextView = itemView.findViewById(R.id.tvLikes)
        val commentCount: TextView = itemView.findViewById(R.id.tvComments)
        val btnLike: ImageView = itemView.findViewById(R.id.btnLike)
        val btnComment: ImageView = itemView.findViewById(R.id.btnComment)
        val btnPlay: ImageView = itemView.findViewById(R.id.btnPlay)

        fun bind(post: Post, position: Int) {
            btnPlay.setOnClickListener {
                if (viewModel.isPlaying.value == true) {
                    viewModel.pauseAudio()
                    btnPlay.setImageResource(R.drawable.play)
                    btnPlay.setColorFilter(Color.parseColor("#27e4F2"))
                } else {
                    viewModel.playAudio(post.recordingPath, position)
                    btnPlay.setImageResource(R.drawable.play_action)
                    btnPlay.setColorFilter(ContextCompat.getColor(btnPlay.context, R.color.red))
                }
            }

            btnLike.setOnClickListener {
                Log.e("NewFeedAdapter click item", "${post.post_id}")
                onFavoriteClick?.invoke(post.post_id)
                if (mlist.contains(post.post_id)) {

                    btnLike.setColorFilter(Color.parseColor("#AAAAAA"))
                } else {
                    btnLike.setColorFilter(Color.parseColor("#FF0000"))
                }
            }
            if (mlist.contains(post.post_id)) {
                btnLike.setColorFilter(Color.parseColor("#FF0000"))
            } else {
                btnLike.setColorFilter(Color.parseColor("#AAAAAA"))
            }
        }
    }

    inner class ShimmerViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if(viewType == TYPE_SHIMMER){
            val view = inflater.inflate(R.layout.item_post_shimmer, parent, false)
            ShimmerViewHolder(view)
        }else {
            val view = inflater.inflate(R.layout.item_post, parent, false)
            NewsFeedViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if(isLoading) 5 else postLists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsFeedViewHolder && !isLoading){
            val post = postLists[position]
            holder.userName.text = post.user.username
            holder.postContent.text = post.title
            holder.postTime.text = conversionTime.formatRelativeTimePretty(post.time)
            holder.likeCount.text = post.likes_count.toString()
            holder.commentCount.text = post.comments_count.toString()
            holder.btnComment.setOnClickListener {
                viewModel.onCommentClicked(post)
            }
            Glide.with(holder.itemView.context)
                .load(post.user.avatar_url)
                .placeholder(R.drawable.user)
                .into(holder.avatar)

            Glide.with(holder.itemView.context)
                .load(post.coverImageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.postImage)

            holder.bind(post, position)

            if (post.statusFromAdmin == "pending") {
                holder.itemView.alpha = 0.5f
                holder.itemView.isEnabled = false
                holder.itemView.isClickable = false
                holder.btnLike.isEnabled = false
                holder.btnComment.isEnabled = false
                holder.btnPlay.isEnabled = false
                holder.avatar.isEnabled = false
                holder.userName.isEnabled = false
                holder.postImage.isEnabled = false
            } else {
                holder.itemView.isEnabled = true
                holder.itemView.isClickable = true
                holder.btnLike.isEnabled = true
                holder.btnComment.isEnabled = true
                holder.btnPlay.isEnabled = true
                holder.avatar.isEnabled = true
                holder.userName.isEnabled = true
                holder.postImage.isEnabled = true
            }

            val profileClickListener = View.OnClickListener {
                post.user.user_id?.let { userId -> onAvatarClick?.invoke(userId) }
            }
            holder.avatar.setOnClickListener(profileClickListener)
            holder.userName.setOnClickListener(profileClickListener)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecordedSonglists(newPostLists: List<Post>) {
        postLists = newPostLists
        notifyDataSetChanged()
    }

    fun setOnAvatarAndNameClick(listener: ((Int) -> Unit)?) {
        onAvatarClick = listener
    }

    fun setFavoriteClick(favoriteListener: ((Int) -> Unit)?) {
        onFavoriteClick = favoriteListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavorited(newList: List<Int>) {
        mlist = newList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLoadingState(isLoadingNow: Boolean) {
        Log.d("Adapter", "updateLoadingState = $isLoadingNow")
        isLoading = isLoadingNow
        notifyDataSetChanged()
    }

}