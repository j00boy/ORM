package com.orm.ui.club

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.orm.databinding.ActivityClubSearchBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.viewmodel.ClubViewModel

class ClubSearchActivity : AppCompatActivity() {
    private val binding: ActivityClubSearchBinding by lazy {
        ActivityClubSearchBinding.inflate(layoutInflater)
    }
    private val clubViewModel: ClubViewModel by viewModels()
    private val rvBoard: RecyclerView by lazy { binding.recyclerView }
    private lateinit var adapter: ProfileBasicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.svClub.isSubmitButtonEnabled = true
//        binding.svClub.setOnQueryTextListener(object :
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(name: String?): Boolean {
//
//                Log.e("onQueryTextSubmit", name.toString())
//                clubViewModel.fetchMountainByName(name.toString())
//                clubViewModel.clubs.observe(this@ClubSearchActivity) { it ->
//                    adapter =
//                        ProfileBasicAdapter(it.map { Club.toRecyclerViewBasicItem(it) })
//
//                    adapter.setItemClickListener(object : ProfileBasicAdapter.OnItemClickListener {
//                        override fun onClick(v: View, position: Int) {
//                            val intent = Intent(
//                                this@ClubSearchActivity,
//                                MountainDetailActivity::class.java
//                            ).apply {
//                                putExtra("club", it[position])
//                            }
//                            startActivity(intent)
//                        }
//                    })
//                    rvBoard.adapter = adapter
//                    rvBoard.layoutManager = LinearLayoutManager(this@ClubSearchActivity)
//                }
//                return false
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                return false
//            }
//
//        })

    }
}