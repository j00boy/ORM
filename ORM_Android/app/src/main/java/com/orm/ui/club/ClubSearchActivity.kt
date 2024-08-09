package com.orm.ui.club

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.data.model.club.Club
import com.orm.databinding.ActivityClubSearchBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.viewmodel.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        clubViewModel.isReady.observe(this@ClubSearchActivity) {
            binding.progressBar.visibility = if (it) View.GONE else View.VISIBLE
        }

        binding.svClub.isSubmitButtonEnabled = true
        binding.svClub.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String?): Boolean {
                clubViewModel.getClubs(name.toString())
                clubViewModel.clubs.observe(this@ClubSearchActivity) { it ->
                    adapter =
                        ProfileBasicAdapter(it.map { Club.toRecyclerViewBasicItem(it) })

                    adapter.setItemClickListener(object : ProfileBasicAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {
                            val intent = Intent(
                                this@ClubSearchActivity,
                                ClubDetailActivity::class.java
                            ).apply {
                                putExtra("club", it[position])
                            }
                            startActivity(intent)
                        }
                    })
                    rvBoard.adapter = adapter
                    rvBoard.layoutManager = LinearLayoutManager(this@ClubSearchActivity)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

    }
}