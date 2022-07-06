package com.experlabs.training.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.experlabs.training.R
import com.experlabs.training.models.Meme
import com.squareup.picasso.Picasso

class MemeAdapter(private val memes: List<Meme>) :
    RecyclerView.Adapter<MemeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.memeitem_name)
        val imageview  = view.findViewById<ImageView>(R.id.memeitem_image)

        init {
            view.setOnClickListener{
                val position : Int = adapterPosition
                Toast.makeText(view.context, "Height=${memes[position].height} Width=${memes[position].width}", Toast.LENGTH_SHORT).show()
            }
        }
    }

     override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
         val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.meme_item, viewGroup, false)

        return ViewHolder(view)
    }

   override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.name.text = memes[position].name
        Picasso.get().load(memes[position].url).into(viewHolder.imageview);
    }

    override fun getItemCount() = memes.size

}