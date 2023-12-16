package com.example.projectq.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectq.data.local.model.ProductDomain
import com.example.projectq.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observe()

        vm.processEvent(MainViewModel.ViewEvent.OnActivityStarted)
    }

    private fun setupRecyclerView() {
        binding.rvProduct.layoutManager = GridLayoutManager(this, 2)
        adapter = MainAdapter()
        binding.rvProduct.adapter = adapter

        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                Toast.makeText(this@MainActivity, id.toString(), Toast.LENGTH_SHORT).show()
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

    private fun showData(data: List<ProductDomain>) {
        adapter.updateList(data)
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}