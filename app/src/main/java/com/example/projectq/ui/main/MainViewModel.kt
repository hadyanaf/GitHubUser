package com.example.projectq.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectq.data.local.model.ThemeUiState
import com.example.projectq.data.util.Resource
import com.example.projectq.data.util.SingleEvent
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.usecase.GetListUserUseCase
import com.example.projectq.domain.usecase.GetListUserUseCaseParam
import com.example.projectq.domain.usecase.GetThemePreferenceUseCase
import com.example.projectq.domain.usecase.SaveAccessTokenUseCase
import com.example.projectq.domain.usecase.SaveAccessTokenUseCaseParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase,
    private val getListUserUseCase: GetListUserUseCase,
    private val getThemePreferenceUseCase: GetThemePreferenceUseCase
) : ViewModel() {

    private val _viewEffect: MutableLiveData<SingleEvent<ViewEffect>> = MutableLiveData()
    val viewEffect: LiveData<SingleEvent<ViewEffect>> = _viewEffect

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
            is ViewEvent.OnActivityStarted -> onActivityStarted(event)
            is ViewEvent.OnCardClicked -> onCardClicked(event)
            is ViewEvent.OnSearchClicked -> onSearchClicked(event)
            ViewEvent.OnFavoriteMenuClicked -> onFavoriteMenuClicked()
            ViewEvent.OnSettingsMenuClicked -> onSettingsMenuClicked()
        }
    }

    private fun onActivityStarted(event: ViewEvent.OnActivityStarted) {
        viewModelScope.launch {
            saveAccessTokenUseCase.execute(SaveAccessTokenUseCaseParam(event.accessToken))
            getUserResponse(event.defaultUsername)
        }
    }

    private fun onCardClicked(event: ViewEvent.OnCardClicked) {
        _viewEffect.value =
            SingleEvent(ViewEffect.NavigateToDetailPage(event.username))
    }

    private fun onSearchClicked(event: ViewEvent.OnSearchClicked) {
        viewModelScope.launch {
            getUserResponse(event.username)
        }
    }

    private fun onFavoriteMenuClicked() {
        _viewEffect.value = SingleEvent(ViewEffect.NavigateToFavoritePage)
    }

    private fun onSettingsMenuClicked() {
        _viewEffect.value = SingleEvent(ViewEffect.NavigateToSettingsPage)
    }

    private suspend fun getUserResponse(username: String) {
        _viewEffect.value =
            SingleEvent(ViewEffect.ShowProgressBar(isVisible = true))
        when (val response =
            getListUserUseCase.execute(GetListUserUseCaseParam(username))) {
            is Resource.Error -> {
                _viewEffect.value =
                    SingleEvent(ViewEffect.ShowErrorMessage(response.message.orEmpty()))
                _viewEffect.value =
                    SingleEvent(ViewEffect.ShowProgressBar(isVisible = false))
            }

            is Resource.Success -> {
                _viewEffect.value =
                    SingleEvent(ViewEffect.ShowData(response.data.orEmpty()))
                _viewEffect.value =
                    SingleEvent(ViewEffect.ShowProgressBar(isVisible = false))
            }
        }
    }

    sealed interface ViewEffect {
        data class ShowData(val data: List<UserHomeDomainModel>) : ViewEffect
        data class ShowErrorMessage(val message: String) : ViewEffect
        data class ShowProgressBar(val isVisible: Boolean) : ViewEffect
        data class NavigateToDetailPage(val username: String) : ViewEffect
        object NavigateToFavoritePage : ViewEffect
        object NavigateToSettingsPage : ViewEffect
    }

    sealed interface ViewEvent {
        data class OnActivityStarted(val accessToken: String, val defaultUsername: String) :
            ViewEvent

        data class OnCardClicked(val username: String) : ViewEvent
        data class OnSearchClicked(val username: String) : ViewEvent
        object OnFavoriteMenuClicked : ViewEvent
        object OnSettingsMenuClicked : ViewEvent
    }
}