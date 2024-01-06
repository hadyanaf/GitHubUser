package com.example.projectq.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectq.R
import com.example.projectq.data.util.IntentConstant
import com.example.projectq.data.util.SwipeToDeleteCallback
import com.example.projectq.databinding.ActivityFavoriteBinding
import com.example.projectq.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    private val vm by viewModels<FavoriteViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observe()
    }

    private fun setupRecyclerView() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteAdapter()
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                vm.processEvent(FavoriteViewModel.ViewEvent.OnCardClicked(username))
            }
        })

        val backgroundColor = ContextCompat.getColor(this, R.color.alertPrimary)
        val icon = ContextCompat.getDrawable(this, R.drawable.ic_trash)

        val swipeHandler = object : SwipeToDeleteCallback(backgroundColor, icon) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val swipedUser = adapter.getItemAtPosition(position)

                swipedUser?.let {
                    vm.processEvent(FavoriteViewModel.ViewEvent.OnCardSwiped(it.username))
                }

                adapter.removeAt(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvUser)
    }

    private fun observe() {
        vm.favoriteUsers.observe(this) {
            adapter.updateList(it)
        }

        vm.viewEffect.observe(this) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { effect ->
                when (effect) {
                    is FavoriteViewModel.ViewEffect.NavigateToDetailPage -> navigateToDetailPage(
                        effect.username
                    )
                }
            }
        }
    }

    private fun navigateToDetailPage(username: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.apply {
            putExtra(IntentConstant.EXTRAS_USERNAME, username)
        }

        startActivity(intent)
    }
}