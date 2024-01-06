package com.example.projectq.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectq.R
import com.example.projectq.data.util.IntentConstant
import com.example.projectq.databinding.ActivityMainBinding
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.ui.detail.DetailActivity
import com.example.projectq.ui.favorite.FavoriteActivity
import com.example.projectq.ui.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val defaultUsername = getString(R.string.default_username)
        val accessToken = getString(R.string.access_token)

        setupSearchView()
        setupRecyclerView()
        observe()

        vm.processEvent(MainViewModel.ViewEvent.OnActivityStarted(accessToken, defaultUsername))
    }

    override fun onResume() {
        super.onResume()
        val defaultUsername = getString(R.string.default_username)
        val accessToken = getString(R.string.access_token)
        vm.processEvent(MainViewModel.ViewEvent.OnActivityStarted(accessToken, defaultUsername))
    }

    private fun setupSearchView() {
        with(binding) {
            searchBar.inflateMenu(R.menu.option_menu)
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchView.hide()
                        vm.processEvent(MainViewModel.ViewEvent.OnSearchClicked(textView.text.toString()))
                        true
                    } else {
                        false
                    }
                }

            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favorite -> {
                        vm.processEvent(MainViewModel.ViewEvent.OnFavoriteMenuClicked)
                        true
                    }

                    R.id.settings -> {
                        vm.processEvent(MainViewModel.ViewEvent.OnSettingsMenuClicked)
                        true
                    }


                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter()
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                vm.processEvent(MainViewModel.ViewEvent.OnCardClicked(username))
            }
        })
    }

    private fun observe() {
        vm.viewEffect.observe(this) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { effect ->
                when (effect) {
                    is MainViewModel.ViewEffect.ShowData -> showData(effect.data)
                    is MainViewModel.ViewEffect.ShowErrorMessage -> showErrorMessage(effect.message)
                    is MainViewModel.ViewEffect.ShowProgressBar -> setProgressViewVisibility(effect.isVisible)
                    is MainViewModel.ViewEffect.NavigateToDetailPage -> navigateToDetailPage(effect.username)
                    MainViewModel.ViewEffect.NavigateToFavoritePage -> navigateToFavoritePage()
                    MainViewModel.ViewEffect.NavigateToSettingsPage -> navigateToSettingsPage()
                }
            }
        }

        vm.uiState.onEach { state ->
            when (state.theme) {
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }.launchIn(lifecycleScope)
    }

    private fun showData(data: List<UserHomeDomainModel>) {
        adapter.updateList(data)
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setProgressViewVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun navigateToDetailPage(username: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.apply {
            putExtra(IntentConstant.EXTRAS_USERNAME, username)
        }

        startActivity(intent)
    }

    private fun navigateToFavoritePage() {
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSettingsPage() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}