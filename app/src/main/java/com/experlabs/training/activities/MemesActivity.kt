package com.experlabs.training.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.experlabs.training.adapters.MemeAdapter
import com.experlabs.training.databinding.ActivityMemesBinding
import com.experlabs.training.models.Meme
import com.experlabs.training.viewmodels.MemeViewModel



class MemesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMemesBinding
    private lateinit var memeViewModel: MemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        memeViewModel = ViewModelProvider(this).get(MemeViewModel::class.java)

        binding.getBt.setOnClickListener {
            binding.memeRecycler.adapter = memeViewModel.memes.value?.memelist?.let{ memes ->
                MemeAdapter(memes){ item ->
                    doThis(item)
                }
            }
        }
    }


    //creating method to make it look simpler
    fun doThis(item: Meme) {
        Toast.makeText(this, "${item.id} Box count=${item.box_count}", Toast.LENGTH_SHORT).show()
    }
}