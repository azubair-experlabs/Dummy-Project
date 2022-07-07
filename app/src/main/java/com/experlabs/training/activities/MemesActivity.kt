package com.experlabs.training.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.experlabs.training.adapters.MemeAdapter
import com.experlabs.training.databinding.ActivityMemesBinding
import com.experlabs.training.models.Meme
import com.experlabs.training.models.Memelist
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
            if (binding.paramEditText.text.isNotEmpty())
                    memeViewModel.getMemesFromRepository(binding.paramEditText.text.toString()){ flag, message ->
                        if (flag) {
                            memeViewModel.memes.observe(this, Observer<Memelist> {
                                it?.memelist?.let { memes ->
                                    binding.memeRecycler.adapter = MemeAdapter(memes) { item ->
                                        doThis(item)
                                    }
                                }
                            })
                            binding.memeRecycler.visibility = View.VISIBLE
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                        else{
                            binding.memeRecycler.visibility = View.GONE
                            Toast.makeText(this, "Failure:$message", Toast.LENGTH_SHORT).show()
                        }
                }
            else
                Toast.makeText(this, "Please enter parameters", Toast.LENGTH_SHORT).show()
        }
    }

    fun doThis(item: Meme) {
        Toast.makeText(this, "${item.id} Box count=${item.box_count}", Toast.LENGTH_SHORT).show()
    }
}