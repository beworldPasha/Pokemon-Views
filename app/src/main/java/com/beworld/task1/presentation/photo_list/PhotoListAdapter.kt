package com.beworld.task1.presentation.photo_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beworld.task1.R
import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.databinding.PhotoItemBinding
import com.beworld.task1.pokemosso.Pokemosso

class PhotoListAdapter(
    private val callback: ItemCallback?
) : RecyclerView.Adapter<PhotoListAdapter.PhotoListViewHolder>() {
    class PhotoListViewHolder(val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var photos = mutableListOf<Photo>()

    interface ItemCallback {
        fun removeClick(photo: Photo, position: Int) {}
        fun openClick(photo: Photo)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotoList(otherPhotoList: List<Photo>) {
        photos = otherPhotoList.toMutableList()
        notifyDataSetChanged()
    }

    fun removePhoto(position: Int) {
        photos.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PhotoItemBinding
            .inflate(inflater, parent, false)

        return PhotoListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        val photo = photos[position]

        holder.binding.tvPhotoName.text = photo.name
        holder.binding.tvPhotoDate.text = photo.date

        photo.uri.let {
            Pokemosso.get()
                .load(it)
                .resize(96, 96)
                .into(holder.binding.ivPhotoImage, object : Pokemosso.Callback {
                    override fun onError() {
                        holder.binding.progressBar.visibility = View.GONE
                        holder.binding.ivPhotoImage.setImageResource(R.drawable.ic_broken_image)
                    }

                    override fun onComplete() {
                        holder.binding.progressBar.visibility = View.GONE
                        holder.binding.ivPhotoImage.visibility = View.VISIBLE
                    }

                    override fun onLoading() {
                        holder.binding.progressBar.visibility = View.VISIBLE
                        holder.binding.ivPhotoImage.visibility = View.GONE
                    }
                })
        }

        holder.binding.btnRemove.setOnClickListener {
            callback?.removeClick(photo, holder.adapterPosition)
        }

        holder.binding.itemContainer.setOnClickListener {
            callback?.openClick(photo)
        }
    }

    override fun getItemCount(): Int = photos.size
}