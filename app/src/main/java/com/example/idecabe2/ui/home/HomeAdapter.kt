package com.example.idecabe2.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.idecabe2.R
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.User
import com.example.idecabe2.databinding.ItemProjectBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class HomeAdapter(
    private val clickHandler: postClickedItem
): RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    //Data Projects
    private var list: MutableList<Project> = arrayListOf()

    inner class MyViewHolder(val binding: ItemProjectBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        fun bind(item: Project){
            binding.projectName.setText(item.name_project)
            binding.textLabel.setText(item.label)
            binding.textCountPhotos.setText("6 photos")
            binding.imageProdukCategory.setImageResource(R.drawable.sample)
        }
        init {
            binding.root.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val currentPosition = list[adapterPosition]
            clickHandler.clickedPostItem(currentPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun editProject(){

    }

    fun edit(){

    }

    fun updateList(list: MutableList<Project>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}