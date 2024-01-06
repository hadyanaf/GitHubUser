package com.example.projectq.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.projectq.data.util.SingleEvent
import com.example.projectq.domain.usecase.DeleteFavoriteUserByUsernameUseCase
import com.example.projectq.domain.usecase.DeleteFavoriteUserByUsernameUseCaseParam
import com.example.projectq.domain.usecase.GetListFavoriteUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val deleteFavoriteUserByUsernameUseCase: DeleteFavoriteUserByUsernameUseCase,
    getListFavoriteUserUseCase: GetListFavoriteUserUseCase
) : ViewModel() {

    private val _viewEffect: MutableLiveData<SingleEvent<ViewEffect>> = MutableLiveData()
    val viewEffect: LiveData<SingleEvent<ViewEffect>> = _viewEffect

    val favoriteUsers = getListFavoriteUserUseCase.execute().asLiveData()

    fun processEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnCardClicked -> onCardClicked(event)
            is ViewEvent.OnCardSwiped -> onCardSwiped(event)
        }
    }

    private fun onCardClicked(event: ViewEvent.OnCardClicked) {
        _viewEffect.value =
            SingleEvent(ViewEffect.NavigateToDetailPage(event.username))
    }

    private fun onCardSwiped(event: ViewEvent.OnCardSwiped) {
        viewModelScope.launch {
            deleteFavoriteUserByUsernameUseCase.execute(
                DeleteFavoriteUserByUsernameUseCaseParam(
                    event.username
                )
            )
        }
    }

    sealed interface ViewEffect {
        data class NavigateToDetailPage(val username: String) : ViewEffect
    }

    sealed interface ViewEvent {
        data class OnCardClicked(val username: String) : ViewEvent
        data class OnCardSwiped(val username: String) : ViewEvent
    }
}