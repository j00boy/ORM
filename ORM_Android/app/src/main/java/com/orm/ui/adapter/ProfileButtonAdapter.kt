package com.orm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orm.R
import com.orm.data.model.recycler.RecyclerViewButtonItem

class ProfileButtonAdapter(private val items: List<RecyclerViewButtonItem>) :
    RecyclerView.Adapter<ProfileButtonAdapter.ProfileButtonViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener

    inner class ProfileButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumbnail = itemView.findViewById<ImageView>(R.id.iv_thumbnail)
        val tvMain = itemView.findViewById<TextView>(R.id.tv_main)
        val tvSub = itemView.findViewById<TextView>(R.id.tv_sub)
        val btnUp = itemView.findViewById<TextView>(R.id.btn_accept)
        val btnDown = itemView.findViewById<Button>(R.id.btn_reject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileButtonViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_button, parent, false)
        return ProfileButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileButtonViewHolder, position: Int) {
        items[position].imageSrc.getNetworkImage(
            holder.itemView.context,
            holder.ivThumbnail
        )
        holder.tvMain.text = items[position].title
        holder.tvSub.text = items[position].subTitle
        holder.btnUp.text = items[position].btnUp
        holder.btnDown.text = items[position].btnDown

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
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .into(view)
    }
}

