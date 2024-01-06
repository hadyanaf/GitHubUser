package com.example.projectq.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.projectq.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val vm by viewModels<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe()
        setListener()
    }

    private fun observe() {
        vm.uiState.onEach { state ->
            when (state.theme) {
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.materialSwitch.isChecked = false
                }

                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.materialSwitch.isChecked = true
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun setListener() {
        binding.materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            val mode =
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            vm.processEvent(SettingsViewModel.ViewEvent.ChangeTheme(mode))
        }
    }
}