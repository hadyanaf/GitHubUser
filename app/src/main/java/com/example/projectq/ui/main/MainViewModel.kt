package com.example.projectq.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectq.data.util.Resource
import com.example.projectq.data.util.SingleEvent
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.usecase.GetListUserUseCase
import com.example.projectq.domain.usecase.GetListUserUseCaseParam
import com.example.projectq.domain.usecase.SaveAccessTokenUseCase
import com.example.projectq.domain.usecase.SaveAccessTokenUseCaseParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase,
    private val getListUserUseCase: GetListUserUseCase
) : ViewModel() {

    private val _viewEffect: MutableLiveData<SingleEvent<ViewEffect>> = MutableLiveData()
    val viewEffect: LiveData<SingleEvent<ViewEffect>> = _viewEffect

    fun processEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnActivityStarted -> onActivityStarted(event)
            is ViewEvent.OnCardClicked -> onCardClicked(event)
        }
    }

    private fun onActivityStarted(event: ViewEvent.OnActivityStarted) {
        viewModelScope.launch {
            saveAccessTokenUseCase.execute(SaveAccessTokenUseCaseParam(event.accessToken))
            when (val response =
                getListUserUseCase.execute(GetListUserUseCaseParam(event.defaultUsername))) {
                is Resource.Error -> {
                    _viewEffect.value =
                        SingleEvent(ViewEffect.ShowErrorMessage(response.message.orEmpty()))
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    _viewEffect.value =
                        SingleEvent(ViewEffect.ShowData(response.data.orEmpty()))
                }
            }
        }
    }

    private fun onCardClicked(event: ViewEvent.OnCardClicked) {

    }

    sealed interface ViewEffect {
        data class ShowData(val data: List<UserHomeDomainModel>) : ViewEffect
        data class ShowErrorMessage(val message: String) : ViewEffect
    }

    sealed interface ViewEvent {
        data class OnActivityStarted(val accessToken: String, val defaultUsername: String) :
            ViewEvent

        data class OnCardClicked(val id: Int) : ViewEvent
    }
}