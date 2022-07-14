package com.experlabs.training.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.experlabs.training.adapters.MemeAdapter
import com.experlabs.training.adapters.MemeMainAdapter
import com.experlabs.training.databinding.ActivityMainBinding
import com.experlabs.training.models.Meme
import com.experlabs.training.viewmodels.MemeViewModel
import io.branch.referral.Branch
import io.branch.referral.BranchError
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(), AndroidScopeComponent {
    private lateinit var adapter: MemeMainAdapter
    private lateinit var binding : ActivityMainBinding
    private lateinit var memeslist : MutableList<Meme>

    override val scope: Scope by activityScope()
    private val memeViewModel: MemeViewModel by inject<MemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apiBt.setOnClickListener {
            val intent = Intent(this, MemesActivity::class.java)
            startActivity(intent)
        }

        binding.deleteAllBt.setOnClickListener {
            memeslist.clear()
            memeViewModel.deleteAllMemesFromRepository()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Deleted All", Toast.LENGTH_SHORT).show()
        }

        memeViewModel.getMemesFromRepository("", false) { _, _ -> }
        memeViewModel.memes.observe(this, Observer<List<Meme>> { data ->
            data?.let { memes ->
                memeslist = memes as MutableList<Meme>

                Toast.makeText(this, "${memes.size}", Toast.LENGTH_SHORT).show()

                adapter = MemeMainAdapter(memeslist) { item, index ->
                    deleteItem(item, index)

                }
                binding.mainRecycler.adapter = adapter
                binding.deleteAllBt.isEnabled = true
            }
        })
    }

    private fun deleteItem(item: Meme, index : Int) {
        memeViewModel.deleteMemeFromRepository(item)
        memeslist.removeAt(index)
        adapter.notifyItemRemoved(index)
        adapter.notifyItemRangeChanged(index, 1)
        Toast.makeText(this, "${item.name} Deleted!", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        // Branch init
        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent

        // if activity is in foreground (or in backstack but partially visible) launch the same
        // activity will skip onStart, handle this case with reInit
        if (intent != null &&
            intent.hasExtra("branch_force_new_session") &&
            intent.getBooleanExtra("branch_force_new_session", false)
        ) {
            Branch.sessionBuilder(this).withCallback(branchListener).reInit()
        }
    }

    object branchListener : Branch.BranchReferralInitListener {
        override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
            if (error == null) {
                Log.i("BRANCH SDK", referringParams.toString())
                // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
            } else {
                Log.e("BRANCH SDK", error.message)
            }
        }
    }
}