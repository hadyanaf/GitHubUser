package com.example.projectq.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectq.data.util.Resource
import com.example.projectq.data.util.SingleEvent
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.usecase.GetListUserFollowersUseCase
import com.example.projectq.domain.usecase.GetListUserFollowersUseCaseParam
import com.example.projectq.domain.usecase.GetListUserFollowingUseCase
import com.example.projectq.domain.usecase.GetListUserFollowingUseCaseParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFollowViewModel @Inject constructor(
    private val getListUserFollowingUseCase: GetListUserFollowingUseCase,
    private val getListUserFollowersUseCase: GetListUserFollowersUseCase
) : ViewModel() {

    private val _viewEffect: MutableLiveData<SingleEvent<ViewEffect>> = MutableLiveData()
    val viewEffect: LiveData<SingleEvent<ViewEffect>> = _viewEffect

    fun processEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnFollowersPage -> onFollowersPage(event)
            is ViewEvent.OnFollowingPage -> onFollowingPage(event)
            is ViewEvent.OnUserClicked -> onUserClicked(event)
        }
    }

    private fun onFollowingPage(event: ViewEvent.OnFollowingPage) {
        viewModelScope.launch {
            _viewEffect.value =
                SingleEvent(ViewEffect.ShowProgressBar(isVisible = true))
            when (val response =
                getListUserFollowingUseCase.execute(GetListUserFollowingUseCaseParam(event.username))) {
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
    }

    private fun onFollowersPage(event: ViewEvent.OnFollowersPage) {
        viewModelScope.launch {
            _viewEffect.value =
                SingleEvent(ViewEffect.ShowProgressBar(isVisible = true))
            when (val response =
                getListUserFollowersUseCase.execute(GetListUserFollowersUseCaseParam(event.username))) {
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
    }

    private fun onUserClicked(event: ViewEvent.OnUserClicked) {
        _viewEffect.value =
            SingleEvent(ViewEffect.NavigateToDetailUser(event.username))
    }

    sealed interface ViewEffect {
        data class ShowData(val data: List<UserHomeDomainModel>) : ViewEffect
        data class ShowErrorMessage(val message: String) : ViewEffect
        data class ShowProgressBar(val isVisible: Boolean) : ViewEffect
        data class NavigateToDetailUser(val username: String) : ViewEffect
    }

    sealed interface ViewEvent {
        data class OnFollowingPage(val username: String) : ViewEvent
        data class OnFollowersPage(val username: String) : ViewEvent
        data class OnUserClicked(val username: String) : ViewEvent
    }
}