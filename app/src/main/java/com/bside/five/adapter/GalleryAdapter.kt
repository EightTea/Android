package com.bside.five.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bside.five.R
import com.bside.five.databinding.LayoutGalleryLowBinding
import com.bside.five.model.GalleryInfo
import com.bside.five.util.GlideUtil

class GalleryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<GalleryInfo>()
    private var beforePointer = 0
    private var listener: ActionListener? = null

    interface ActionListener {
        fun clickItem(position: Int, item: GalleryInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutGalleryLowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LowViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LowViewHolder) {
            holder.bind(items[position], listener)
        }
    }

    inner class LowViewHolder(val binding: LayoutGalleryLowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GalleryInfo, listener: ActionListener?) {
            binding.apply {
                GlideUtil.loadImage(imageView, item.uri)

                if (item.isCheck) {
                    GlideUtil.loadImage(imgCheck, R.drawable.ic_check_selected)
                } else {
                    GlideUtil.loadImage(imgCheck, R.drawable.ic_check_enabled)
                }

                root.setOnClickListener {
                    items[beforePointer].isCheck = false
                    notifyItemChanged(beforePointer)

                    items[bindingAdapterPosition].isCheck = true
                    notifyItemChanged(bindingAdapterPosition)
                    beforePointer = bindingAdapterPosition

                    listener?.clickItem(bindingAdapterPosition, item)
                }
            }
        }
    }

    fun replaceAll(list: ArrayList<GalleryInfo>) {
        items.apply {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }

    fun setActionListener(listener: ActionListener) {
        this.listener = listener
    }
}