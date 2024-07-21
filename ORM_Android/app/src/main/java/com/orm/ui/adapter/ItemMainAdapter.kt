package com.orm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orm.R
import com.orm.data.model.RecyclerViewItem

class ItemMainAdapter(private val items: List<RecyclerViewItem>) :
    RecyclerView.Adapter<ItemMainAdapter.ItemMainViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener

    inner class ItemMainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_thumbnail = itemView.findViewById<ImageView>(R.id.iv_thumbnail)
        val tv_main = itemView.findViewById<TextView>(R.id.tv_main)
        val tv_sub = itemView.findViewById<TextView>(R.id.tv_sub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMainViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_profile, parent, false)
        return ItemMainViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemMainViewHolder, position: Int) {
        items[position].imageSrc.getNetworkImage(
            holder.itemView.context,
            holder.iv_thumbnail
        )
        holder.tv_main.text = items[position].title
        holder.tv_sub.text = items[position].subTitle

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
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

    private fun String.getNetworkImage(context: Context, view: ImageView) {
        Glide.with(context)
            .load(this)
            .centerCrop()
            .into(view)
    }
}

