package com.hoc.example.ui.main

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.hoc.example.R
import com.hoc.example.data.Photo
import kotlinx.android.synthetic.main.photo_item_layout.view.*

class PhotoAdapter : PagedListAdapter<Photo, PhotoAdapter.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item_layout, parent, false)
            .let(::ViewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val imageThumbnail = itemView.image_thumbnail!!
        private val textTitle = itemView.text_title!!

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            adapterPosition
                .takeIf { it != RecyclerView.NO_POSITION }
                ?.let(::getItem)
                ?.let {
                    MainFragmentDirections.ActionMainFragmentToDetailFragment(it)
                        .let { v.findNavController().navigate(it) }
                }
        }

        fun bind(item: Photo?) = item?.let { photo ->
            textTitle.text = photo.title
            Glide.with(itemView.context)
                .load(photo.thumbnailUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_image_black_24dp))
                .apply(RequestOptions.fitCenterTransform())
                .into(imageThumbnail)
        }
    }

    companion object {
        @JvmField
        val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
        }
    }
}