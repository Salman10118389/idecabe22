package com.example.idecabe2.ui.projectDetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.idecabe2.databinding.ItemPhotoBinding

class ImageListingAdapter(private val list: List<Uri>): RecyclerView.Adapter<ImageListingAdapter.MyViewHolder>() {

    inner class MyViewHolder( val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Uri, position: Int){
            binding.imageProdukCategory.setImageURI(item)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}