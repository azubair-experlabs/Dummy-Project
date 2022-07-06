package com.experlabs.training.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.experlabs.training.adapters.MemeAdapter
import com.experlabs.training.databinding.ActivityMemesBinding
import com.experlabs.training.repository.MemeRepository
import com.experlabs.training.retrofit.ApiService
import com.experlabs.training.retrofit.RetrofitObject
import com.experlabs.training.viewmodels.MemeViewModel
import com.experlabs.training.viewmodels.MemeViewModelFactory


class MemesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMemesBinding
    private lateinit var memeViewModel: MemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val api = RetrofitObject.getInstance().create(ApiService::class.java)
        val repository = MemeRepository(api)

        memeViewModel = ViewModelProvider(this, MemeViewModelFactory(repository)).get(MemeViewModel::class.java)

        binding.getBt.setOnClickListener {
            binding.memeRecycler.adapter = memeViewModel.memes.value?.memelist?.let{ memes ->
                MemeAdapter(memes)
            }
        }
//        Log.i("API result", memelist.toString())

//        val callback = object : Callback<Data?> {
//            override fun onFailure(call: Call<Data?>, t: Throwable) {
//                Toast.makeText(
//                    this@MemesActivity, "Api Call Failed!", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
//                Toast.makeText(this@MemesActivity, "Successful!", Toast.LENGTH_SHORT)
//                    .show()
//                response.body()?.memes?.memelist?.also {
//                    runOnUiThread {
//                        binding.memeRecycler.adapter = MemeAdapter(it)
//                    }
//                }
//            }
//        }
    }
}