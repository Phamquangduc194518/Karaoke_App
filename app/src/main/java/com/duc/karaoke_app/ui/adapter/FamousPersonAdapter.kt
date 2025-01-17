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
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.model.User
import com.duc.karaoke_app.data.model.UserResponse

class FamousPersonAdapter() : RecyclerView.Adapter<FamousPersonAdapter.FamousPersonViewHolder>() {
    private var famousPersonList: List<User> = listOf()
    private var onItemClick : ((User) -> Unit)? = null
    class FamousPersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
          val coverPerson = itemView.findViewById<ImageView>(R.id.ivCoverPerson)
          val personTitle = itemView.findViewById<TextView>(R.id.tvPersonTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamousPersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_famous_person, parent, false)
        return FamousPersonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return famousPersonList.size
    }

    override fun onBindViewHolder(holder: FamousPersonViewHolder, position: Int) {
         val famousPerson = famousPersonList[position]
         Glide.with(holder.itemView.context)
             .load(famousPerson.avatarUrl)
             .placeholder(R.drawable.placeholder_image)
             .error(R.drawable.placeholder_image)
             .into(holder.coverPerson)
         holder.personTitle.text = famousPerson.username
         holder.itemView.setOnClickListener{
            onItemClick.let{
                onItemClick?.invoke(famousPerson)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateFamousPerson(newPersonList: List<User>){
        this.famousPersonList = newPersonList
        notifyDataSetChanged()
    }

    fun setOnItemClickOfFamous(listener: ((User) -> Unit)?) {
        onItemClick = listener
    }
}