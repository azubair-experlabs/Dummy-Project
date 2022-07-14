package com.experlabs.training.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.room.RoomDatabase
import com.experlabs.training.adapters.MemeAdapter
import com.experlabs.training.databinding.ActivityMemesBinding
import com.experlabs.training.models.Meme
import com.experlabs.training.models.Memelist
import com.experlabs.training.room.MemeDatabase
import com.experlabs.training.viewmodels.MemeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope
import java.lang.Exception


class MemesActivity : AppCompatActivity(), AndroidScopeComponent {

    private lateinit var binding : ActivityMemesBinding

    lateinit var database : MemeDatabase

    override val scope: Scope by activityScope()
    private val memeViewModel: MemeViewModel by inject<MemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = MemeDatabase.getInstance(this)!!

        binding.getBt.setOnClickListener {
            if (binding.paramEditText.text.isNotEmpty())
                    memeViewModel.getMemesFromRepository(binding.paramEditText.text.toString(), true){ flag, message ->
                        if (flag) {
                            memeViewModel.memes.observe(this, Observer<List<Meme>> { data ->
                                data?.let { memes ->

                                    CoroutineScope(Dispatchers.IO).launch{
                                        try {
                                            database.dao()?.insertAll(memes)
                                        }
                                        catch (e : Exception){
                                            Log.i("Room", e.toString())
                                        }
                                        }

                                    val list = memes.map {
                                        val temp = it
                                        temp.name = "Meme Title: ${it.name}"
                                        temp.width = "Width: ${it.width}"
                                        temp.height = "Height: ${it.height}"
                                        temp }

                                    binding.memeRecycler.adapter = MemeAdapter(list) { item ->
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