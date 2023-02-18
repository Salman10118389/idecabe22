package com.example.idecabe2.ui.projectDetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.idecabe2.databinding.ItemPhotoBinding

class ImageListingAdapter: RecyclerView.Adapter<ImageListingAdapter.MyViewHolder>() {

    private var list: MutableList<Uri> = arrayListOf()

    inner class MyViewHolder( val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Uri, position: Int){
            binding.imageProdukCategory.setImageURI(item)
        }
    }

    fun onUpdateList(list: MutableList<Uri>){
        this.list = list
        notifyDataSetChanged()
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