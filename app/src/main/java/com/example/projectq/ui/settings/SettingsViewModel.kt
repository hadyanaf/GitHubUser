package com.example.projectq.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectq.data.local.model.ThemeUiState
import com.example.projectq.domain.usecase.GetThemePreferenceUseCase
import com.example.projectq.domain.usecase.SetThemePreferenceUseCase
import com.example.projectq.domain.usecase.SetThemePreferenceUseCaseParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getThemePreferenceUseCase: GetThemePreferenceUseCase,
    private val setThemePreferenceUseCase: SetThemePreferenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThemeUiState())
    val uiState: StateFlow<ThemeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getThemePreferenceUseCase.execute().collect { theme ->
                _uiState.value = ThemeUiState(theme)
            }
        }
    }

    fun processEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.ChangeTheme -> setThemePreference(event.themeMode)
        }
    }

    private fun setThemePreference(themeMode: Int) {
        viewModelScope.launch {
            setThemePreferenceUseCase.execute(SetThemePreferenceUseCaseParam(themeMode))
        }
    }

    sealed class ViewEvent {
        data class ChangeTheme(val themeMode: Int) : ViewEvent()
    }
}
