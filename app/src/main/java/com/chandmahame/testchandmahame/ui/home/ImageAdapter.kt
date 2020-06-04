package com.chandmahame.testchandmahame.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.chandmahame.testchandmahame.R
import com.chandmahame.testchandmahame.model.Dairy
import kotlinx.android.synthetic.main.image_item_rcy.view.*

class ImageAdapter(private val requestManager: RequestManager,private val type:Int) : ListAdapter<Dairy, ImageAdapter.ViewHolder>(DiffCallback()) {

    companion object{
        const val SERVER_IMAGE=1
        const val LOCAL_IMAGE=2
    }
    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Dairy, requestManager: RequestManager,type: Int) {
            if(type== SERVER_IMAGE)
            requestManager.load(item.imagePath)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .override(200,200)
                .into(itemView.img)
            else
                requestManager.load(item.imagePath.toUri())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(itemView.img)
            itemView.txt_title.text=item.title

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Dairy>() {
        override fun areItemsTheSame(oldItem: Dairy, newItem: Dairy): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dairy, newItem: Dairy): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.image_item_rcy, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, requestManager,type)
    }

    fun clearList() {
        submitList(ArrayList())
    }
}