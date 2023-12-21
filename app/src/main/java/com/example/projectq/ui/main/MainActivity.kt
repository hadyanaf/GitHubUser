package com.example.projectq.ui.main

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectq.R
import com.example.projectq.databinding.ActivityMainBinding
import com.example.projectq.domain.model.UserHomeDomainModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchView.hide()
                        Timber.d("Check text: ${textView.text}")
                        true
                    } else {
                        false
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
                Toast.makeText(this@MainActivity, username, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observe() {
        vm.viewEffect.observe(this) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { effect ->
                when (effect) {
                    is MainViewModel.ViewEffect.ShowData -> showData(effect.data)
                    is MainViewModel.ViewEffect.ShowErrorMessage -> showErrorMessage(effect.message)
                }
            }
        }
    }

    private fun showData(data: List<UserHomeDomainModel>) {
        adapter.updateList(data)
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}