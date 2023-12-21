package com.example.projectq.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectq.data.util.IntentConstant
import com.example.projectq.databinding.FragmentDetailFollowBinding
import com.example.projectq.domain.model.UserHomeDomainModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFollowFragment : Fragment() {

    private lateinit var binding: FragmentDetailFollowBinding
    private lateinit var adapter: DetailFollowAdapter

    private val vm by viewModels<DetailFollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 0
        var username = ""

        arguments?.let { bundle ->
            bundle.getInt(IntentConstant.ARG_POSITION, 0).let { pos ->
                position = pos
            }
            bundle.getString(IntentConstant.ARG_USERNAME)?.let { name ->
                username = name
            }
        }

        setupLayout(position, username)
        setupListener(position, username)
        setupRecyclerView()
        observe()
    }

    private fun setupLayout(position: Int, username: String) {
        if (position == 0) {
            vm.processEvent(DetailFollowViewModel.ViewEvent.OnFollowersPage(username))
        } else {
            vm.processEvent(DetailFollowViewModel.ViewEvent.OnFollowingPage(username))
        }

        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupListener(position: Int, username: String) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            setupLayout(position, username)
        }
    }

    private fun setupRecyclerView() {
        context?.let {
            binding.rvUser.layoutManager = LinearLayoutManager(it)
            adapter = DetailFollowAdapter()
            binding.rvUser.adapter = adapter

            adapter.setOnItemClickCallback(object : DetailFollowAdapter.OnItemClickCallback {
                override fun onItemClicked(username: String) {
                    vm.processEvent(DetailFollowViewModel.ViewEvent.OnUserClicked(username))
                }
            })
        }
    }

    private fun observe() {
        vm.viewEffect.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { effect ->
                when (effect) {
                    is DetailFollowViewModel.ViewEffect.NavigateToDetailUser -> navigateToDetailPage(
                        effect.username
                    )

                    is DetailFollowViewModel.ViewEffect.ShowData -> showData(effect.data)
                    is DetailFollowViewModel.ViewEffect.ShowErrorMessage -> showErrorMessage(effect.message)
                    is DetailFollowViewModel.ViewEffect.ShowProgressBar -> setProgressViewVisibility(
                        effect.isVisible
                    )
                }
            }
        }
    }

    private fun showData(data: List<UserHomeDomainModel>) {
        adapter.updateList(data)
    }

    private fun showErrorMessage(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setProgressViewVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun navigateToDetailPage(username: String) {
        context?.let {
            val intent = Intent(it, DetailActivity::class.java)
            intent.apply {
                putExtra(IntentConstant.EXTRAS_USERNAME, username)
            }

            startActivity(intent)
        }
    }
}