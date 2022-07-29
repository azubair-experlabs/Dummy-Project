package com.experlabs.training.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.experlabs.training.adapters.MemeMainAdapter
import com.experlabs.training.adapters.SnapHelperOneByOne
import com.experlabs.training.databinding.ActivityMainBinding
import com.experlabs.training.fcm.services.FirebaseMessagingService
import com.experlabs.training.models.Meme
import com.experlabs.training.viewmodels.MemeViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import io.branch.referral.Branch
import io.branch.referral.BranchError
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope


class MainActivity : AppCompatActivity(), AndroidScopeComponent {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var adapter: MemeMainAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var memeslist: MutableList<Meme>
    private var index = 0
    private var branch_flag = false

    override val scope: Scope by activityScope()
    private val memeViewModel: MemeViewModel by inject<MemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createToken()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val data = this.intent.data
        if (data != null && data.isHierarchical) {
            val uri = this.intent.dataString
            Log.i("trainingproject.com", "Deep link clicked $uri")
        }

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        branchListener.branchResponse{flag, value ->
            branch_flag = flag
            index = value
            binding.mainRecycler.scrollToPosition(index)
            Toast.makeText(this, "Scrolled to $index", Toast.LENGTH_SHORT).show()
        }

        binding.crashBt.setOnClickListener {
            throw RuntimeException("Test Crash")
        }

        binding.apiBt.setOnClickListener {
            val intent = Intent(this, MemesActivity::class.java)
            startActivity(intent)
        }

        binding.deleteAllBt.setOnClickListener {
            memeslist.clear()
            memeViewModel.deleteAllMemesFromRepository() { flag, message ->

                if (flag)
                    adapter.notifyDataSetChanged()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }

        }

        memeViewModel.getMemesFromRepository("", false) { flag, message ->

            if (flag) {
                memeViewModel.memes.observe(this, Observer<List<Meme>> { data ->
                    data?.let { memes ->
                        memeslist = memes as MutableList<Meme>

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                        adapter = MemeMainAdapter(memeslist) { item, index ->
                            deleteItem(item, index)

                        }
                        val snaper = SnapHelperOneByOne()
                        snaper.attachToRecyclerView(binding.mainRecycler)
                        binding.mainRecycler.adapter = adapter

                        binding.deleteAllBt.isEnabled = true
                    }
                })
            } else {
                binding.mainRecycler.adapter = null
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun deleteItem(item: Meme, index: Int) {
        memeViewModel.deleteMemeFromRepository(item) { flag, message ->

            if (flag) {
                memeslist.removeAt(index)
                adapter.notifyItemRemoved(index)
                adapter.notifyItemRangeChanged(index, 1)
                Toast.makeText(this, "${item.name} : $message!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isComplete){
                val token = it.result.toString()
                FirebaseMessagingService.addTokenToFirestore(token)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Branch init
        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data)
            .init()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent

        // if activity is in foreground (or in backstack but partially visible) launch the same
        // activity will skip onStart, handle this case with reInit
        if (intent.hasExtra("branch_force_new_session") && intent.getBooleanExtra("branch_force_new_session", false)) {
            Branch.sessionBuilder(this).withCallback(branchListener).reInit()
        }
    }

    object branchListener : Branch.BranchReferralInitListener {

        lateinit var response : (Boolean, Int) -> Unit

        override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
            if (error == null) {
                Log.i("BRANCH SDK", referringParams.toString())

                referringParams?.let {
                    if (it.getString("+clicked_branch_link").toBoolean()) {
                        it.getString("index").let { value ->
                            response(true, value.toInt())
                            return
                        }
                    }
                }
                response(false, 0)

            // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
            } else {
                response(false, 0)
                Log.e("BRANCH SDK", error.message)
            }
        }

        fun branchResponse(callback : (Boolean, Int) -> Unit) {
            response = callback
        }
    }
}