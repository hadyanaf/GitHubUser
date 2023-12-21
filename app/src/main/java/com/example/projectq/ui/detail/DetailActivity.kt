package com.example.projectq.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.projectq.R
import com.example.projectq.data.util.IntentConstant
import com.example.projectq.databinding.ActivityDetailBinding
import com.example.projectq.domain.model.UserDetailDomainModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val vm: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(IntentConstant.EXTRAS_USERNAME).orEmpty()
        vm.processEvent(DetailViewModel.ViewEvent.OnActivityStarted(username))

        observe()
    }

    private fun setupViewPager(username: String, followingCount: Int, followersCount: Int) {
        with(binding) {
            val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailActivity)
            sectionsPagerAdapter.setUsername(username)
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                val followCount = if (position == 0) followersCount else followingCount
                tab.text = resources.getString(TAB_TITLES[position], followCount)
            }.attach()
            supportActionBar?.elevation = 0f
        }
    }

    private fun observe() {
        vm.viewEffect.observe(this) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { effect ->
                when (effect) {
                    is DetailViewModel.ViewEffect.ShowData -> showData(effect.data)
                    is DetailViewModel.ViewEffect.ShowErrorMessage -> showErrorMessage(effect.message)
                    is DetailViewModel.ViewEffect.ShowProgressBar -> setProgressViewVisibility(
                        effect.isVisible
                    )
                }
            }
        }
    }

    private fun showData(data: UserDetailDomainModel) {
        with(binding) {
            Glide.with(this@DetailActivity)
                .load(data.avatarUrl)
                .into(imgAvatar)

            tvFullname.text = data.fullName
            tvUsername.text = data.username

            setupViewPager(
                username = data.username.orEmpty(),
                followingCount = data.followingCount ?: 0,
                followersCount = data.followersCount ?: 0
            )
        }
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}