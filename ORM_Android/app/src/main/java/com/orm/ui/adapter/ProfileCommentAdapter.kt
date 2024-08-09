package com.orm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orm.R
import com.orm.data.model.recycler.RecyclerViewCommentItem

class ProfileCommentAdapter(
    private val items: List<RecyclerViewCommentItem>,
    private val onEditClick: (RecyclerViewCommentItem) -> Unit,
    private val onDeleteClick: (RecyclerViewCommentItem) -> Unit
) :RecyclerView.Adapter<ProfileCommentAdapter.ProfileCommentViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener

    inner class ProfileCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        val tvUserNickname = itemView.findViewById<TextView>(R.id.tv_userNickname)
        val tvCreatdAt = itemView.findViewById<TextView>(R.id.tv_createdAt)
        val tvEdit: TextView = itemView.findViewById(R.id.tv_edit)
        val tvDelete: TextView = itemView.findViewById(R.id.tv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCommentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_comment, parent, false)
        return ProfileCommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileCommentViewHolder, position: Int) {
        val item = items[position]
        holder.tvContent.text = items[position].content
        holder.tvUserNickname.text = items[position].userNickname
        holder.tvCreatdAt.text = items[position].createdAt
        holder.tvEdit.setOnClickListener {
            onEditClick(item)
        }

        holder.tvDelete.setOnClickListener {
            onDeleteClick(item)
        }

    }

    override fun getItemCount(): Int {
        return items.count()
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}