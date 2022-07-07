package com.experlabs.training.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.experlabs.training.databinding.MemeItemBinding
import com.experlabs.training.models.Meme
import com.squareup.picasso.Picasso

class MemeAdapter(private val memes: List<Meme>, val adapterOnClick : (Meme) -> Unit) : RecyclerView.Adapter<MemeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : MemeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Meme){
            binding.memeitem = item
        }

        init {
            itemView.setOnClickListener{
                val position : Int = adapterPosition
                adapterOnClick(memes[position])
            }
        }
    }

     override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
         val inflater = LayoutInflater.from(viewGroup.context)
         val itembinding = MemeItemBinding.inflate(inflater, viewGroup, false)
         return ViewHolder(itembinding)
    }

   override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(memes[position])}

    override fun getItemCount() = memes.size

}

@BindingAdapter("imageFromURL")
fun ImageView.imageFromURL(url : String){
    Picasso.get().load(url).into(this);
}