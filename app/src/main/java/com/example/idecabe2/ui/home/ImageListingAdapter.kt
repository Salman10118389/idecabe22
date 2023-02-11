package com.example.idecabe2.ui.home

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.idecabe2.databinding.IconImageBinding

class ImageListingAdapter(val onCancelClicked: ((Int, Uri) -> Unit)): RecyclerView.Adapter<ImageListingAdapter.MyViewHolder>() {

    private var imageList: MutableList<Uri> = arrayListOf()

    inner class MyViewHolder(val binding: IconImageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Uri, position: Int){
            binding.iconPhoto.setImageURI(item)
            binding.cancel.setOnClickListener {
                onCancelClicked?.invoke(adapterPosition, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}