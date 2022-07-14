package com.experlabs.training.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.experlabs.training.databinding.MemeMainItemBinding
import com.experlabs.training.models.Meme

class MemeMainAdapter(private val memes: MutableList<Meme>, val deleteOnClick : (Meme, Int) -> Unit) : RecyclerView.Adapter<MemeMainAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : MemeMainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Meme, position: Int){
            binding.memeitem = item

            binding.deleteBt.setOnClickListener {
                deleteOnClick((memes[position]), position)
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itembinding = MemeMainItemBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(itembinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(memes[position],position)}

    override fun getItemCount() = memes.size

}