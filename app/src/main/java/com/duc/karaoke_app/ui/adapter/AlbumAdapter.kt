package com.duc.karaoke_app.ui.adapter

class AlbumAdapter(
    private val albums: List<Album>,
    private val onClick: (Album) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAlbumCover: ImageView = itemView.findViewById(R.id.ivAlbumCover)
        val tvAlbumTitle: TextView = itemView.findViewById(R.id.tvAlbumTitle)
        val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]

        // Gán dữ liệu
        holder.tvAlbumTitle.text = album.title
        holder.tvArtistName.text = album.artist

        // Load ảnh album từ URL hoặc drawable
        Glide.with(holder.itemView.context)
            .load(album.coverUrl)
            .placeholder(R.drawable.rounded_background)
            .into(holder.ivAlbumCover)

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener { onClick(album) }
    }

    override fun getItemCount(): Int = albums.size
}
