package com.experlabs.training.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.experlabs.training.adapters.MemeAdapter
import com.experlabs.training.databinding.ActivityMemesBinding
import com.experlabs.training.models.Meme
import com.experlabs.training.models.Memelist
import com.experlabs.training.viewmodels.MemeViewModel
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope


class MemesActivity : AppCompatActivity(), AndroidScopeComponent {

    private lateinit var binding : ActivityMemesBinding

    override val scope: Scope by activityScope()
    private val memeViewModel: MemeViewModel by inject<MemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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