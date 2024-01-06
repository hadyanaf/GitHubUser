package com.example.projectq.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projectq.R
import com.example.projectq.data.util.IntentConstant
import com.example.projectq.databinding.ActivityDetailBinding
import com.example.projectq.domain.model.UserDetailDomainModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var userId: Int? = null
    private var avatarUrl: String? = null
    private var username: String? = null
    private var isFavorite: Boolean? = null

    private val vm: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(IntentConstant.EXTRAS_USERNAME).orEmpty()
        vm.processEvent(DetailViewModel.ViewEvent.OnActivityStarted(username))

        observe()
        setupListener()
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

        vm.favoriteUser.observe(this) { data ->
            isFavorite = data != null
            updateFavoriteIcon()
        }
    }

    private fun setupListener() {
        binding.icFavorite.setOnClickListener {
            Timber.d("Check favorite button clicked")
            if (isFavorite == true) {
                vm.processEvent(
                    DetailViewModel.ViewEvent.OnFavoriteButtonClicked(
                        id = userId,
                        avatarUrl = avatarUrl,
                        username = username,
                        isFavorite = false
                    )
                )
            } else {
                vm.processEvent(
                    DetailViewModel.ViewEvent.OnFavoriteButtonClicked(
                        id = userId,
                        avatarUrl = avatarUrl,
                        username = username,
                        isFavorite = true
                    )
                )
            }
        }

    }

    private fun showData(data: UserDetailDomainModel) {
        with(binding) {
            avatarUrl = data.avatarUrl
            username = data.username
            userId = data.id

            Glide.with(this@DetailActivity)
                .load(data.avatarUrl)
                .into(imgAvatar)

            tvFullname.text = data.fullName
            tvUsername.text = username

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

    private fun updateFavoriteIcon() {
        val drawableId =
            if (isFavorite == true) R.drawable.ic_favorite_true else R.drawable.ic_favorite_false
        binding.icFavorite.setImageDrawable(ContextCompat.getDrawable(this, drawableId))
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}