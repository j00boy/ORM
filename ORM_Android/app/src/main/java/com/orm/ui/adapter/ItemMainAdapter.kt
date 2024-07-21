package com.orm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orm.R
import com.orm.data.model.RecyclerViewItem

class ItemMainAdapter(private val itemList: List<RecyclerViewItem>) :
    RecyclerView.Adapter<ItemMainAdapter.ItemMainViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMainViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_profile, parent, false)
        return ItemMainViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemMainViewHolder, position: Int) {
//        holder.img_thumnail
        holder.tv_main.text = itemList[position].title
        holder.tv_sub.text = itemList[position].subTitle

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


    inner class ItemMainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img_thumnail = itemView.findViewById<ImageView>(R.id.img_thumnail)
        val tv_main = itemView.findViewById<TextView>(R.id.tv_main)
        val tv_sub = itemView.findViewById<TextView>(R.id.tv_sub)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }


}