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
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.model.AccountWithFollowers
import com.duc.karaoke_app.data.model.Following
import com.duc.karaoke_app.data.model.FollowingStar
import com.duc.karaoke_app.data.model.User

class FamousPersonAdapter() : RecyclerView.Adapter<FamousPersonAdapter.FamousPersonViewHolder>() {
    private var famousPersonList: List<AccountWithFollowers> = listOf()
    private var onItemClick: ((FollowingStar) -> Unit)? = null

    class FamousPersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverPerson = itemView.findViewById<ImageView>(R.id.ivCoverPerson)
        val personTitle = itemView.findViewById<TextView>(R.id.tvPersonTitle)
        val vLiveBadge = itemView.findViewById<View>(R.id.v_LiveBadge)
        val tvLiveBadge = itemView.findViewById<TextView>(R.id.tv_LiveBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamousPersonViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_famous_person, parent, false)
        return FamousPersonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return famousPersonList.size
    }

    override fun onBindViewHolder(holder: FamousPersonViewHolder, position: Int) {
        val famousPerson = famousPersonList[position]
        Glide.with(holder.itemView.context)
            .load(famousPerson.following.avatarUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.coverPerson)
        holder.personTitle.text = famousPerson.following.username
        holder.tvLiveBadge.visibility = View.GONE
        holder.vLiveBadge.visibility = View.GONE
        val liveStatus = famousPerson.following.liveStream?.status ?: "ended"
        if (liveStatus == "active") {
            holder.tvLiveBadge.visibility = View.VISIBLE
            holder.vLiveBadge.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            onItemClick.let {
                if(liveStatus == "active")
                    onItemClick?.invoke(famousPerson.following)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateFamousPerson(newPersonList: List<AccountWithFollowers>) {
        this.famousPersonList = newPersonList
        notifyDataSetChanged()
    }

    fun setOnItemClickOfFamous(listener: ((FollowingStar) -> Unit)?) {
        onItemClick = listener
    }
}