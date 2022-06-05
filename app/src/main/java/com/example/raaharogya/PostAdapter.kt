package com.example.raaharogya


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.raaharogya.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener: IPostAdapter) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    options
) {

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val locationText: TextView = itemView.findViewById(R.id.locationTextView)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val upVotedCount: TextView = itemView.findViewById(R.id.upvoteTextView)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val upVoteButton: ImageView = itemView.findViewById(R.id.upvoteImageView)
        val mapButton: ImageView = itemView.findViewById(R.id.mapButton)
        val potholeImage: ImageView = itemView.findViewById(R.id.potHoleImageView)
        val status: TextView = itemView.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
        viewHolder.upVoteButton.setOnClickListener{
            listener.onUpVoted(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.mapButton.setOnClickListener {
            listener.onMapClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.locationText.text = model.text
        holder.userText.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        Glide.with(holder.potholeImage.context).load(model.imageUrl).into(holder.potholeImage)
        holder.upVotedCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        if(model.status == 0){
            holder.status.setTextColor(Color.RED)
            holder.status.text = " Pending"
        }
        else if(model.status == 1){
            holder.status.setTextColor(Color.BLUE)
            holder.status.text = " Work in Progress"
        }
        else if(model.status == 2){
            holder.status.setTextColor(Color.GREEN)
            holder.status.text = " Completed"
        }

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        if(isLiked) {
            holder.upVoteButton.setImageDrawable(ContextCompat.getDrawable(holder.upVoteButton.context, R.drawable.ic_upvote_red))
        } else {
            holder.upVoteButton.setImageDrawable(ContextCompat.getDrawable(holder.upVoteButton.context, R.drawable.ic_upvote_black))
        }

    }
}

interface IPostAdapter {
    fun onUpVoted(postId: String)
    fun onMapClicked(postId: String)
}