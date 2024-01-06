package com.example.projectq.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.projectq.data.util.Resource
import com.example.projectq.data.util.SingleEvent
import com.example.projectq.domain.model.UserDetailDomainModel
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.usecase.DeleteFavoriteUserByUsernameUseCase
import com.example.projectq.domain.usecase.DeleteFavoriteUserByUsernameUseCaseParam
import com.example.projectq.domain.usecase.GetFavoriteUserUseCase
import com.example.projectq.domain.usecase.GetFavoriteUserUseCaseParam
import com.example.projectq.domain.usecase.GetUserDetailUseCase
import com.example.projectq.domain.usecase.GetUserDetailUseCaseParam
import com.example.projectq.domain.usecase.InsertFavoriteUserUseCase
import com.example.projectq.domain.usecase.InsertFavoriteUserUseCaseParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getFavoriteUserUseCase: GetFavoriteUserUseCase,
    private val insertFavoriteUserUseCase: InsertFavoriteUserUseCase,
    private val deleteFavoriteUserByUsernameUseCase: DeleteFavoriteUserByUsernameUseCase
) : ViewModel() {

    private val _viewEffect: MutableLiveData<SingleEvent<ViewEffect>> = MutableLiveData()
    val viewEffect: LiveData<SingleEvent<ViewEffect>> = _viewEffect

    private val _username = MutableLiveData<String>()

    val favoriteUser = _username.switchMap { username ->
        getFavoriteUserUseCase.execute(GetFavoriteUserUseCaseParam(username)).asLiveData()
    }

    fun processEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnActivityStarted -> onActivityStarted(event)
            is ViewEvent.OnFavoriteButtonClicked -> onFavoriteButtonClicked(event)
        }
    }

    private fun onActivityStarted(event: ViewEvent.OnActivityStarted) {
        viewModelScope.launch {
            _viewEffect.value =
                SingleEvent(ViewEffect.ShowProgressBar(isVisible = true))
            when (val response =
                getUserDetailUseCase.execute(GetUserDetailUseCaseParam(event.username))) {
                is Resource.Error -> {
                    _viewEffect.value =
                        SingleEvent(ViewEffect.ShowErrorMessage(response.message.orEmpty()))
                    _viewEffect.value =
                        SingleEvent(ViewEffect.ShowProgressBar(isVisible = false))
                }

                is Resource.Success -> {
                    _viewEffect.value =
                        SingleEvent(ViewEffect.ShowData(response.data ?: UserDetailDomainModel()))
                    _viewEffect.value =
                        SingleEvent(ViewEffect.ShowProgressBar(isVisible = false))
                    _username.value = event.username
                }
            }
        }
    }

    private fun onFavoriteButtonClicked(event: ViewEvent.OnFavoriteButtonClicked) {
        viewModelScope.launch {
            val (id, avatarUrl, username, isFavorite) = event
            Timber.d("Check insert data: $id, $avatarUrl, $username, $isFavorite")
            if (isFavorite) {
                val favoriteUser =
                    UserHomeDomainModel(id ?: 0, avatarUrl.orEmpty(), username.orEmpty())
                insertFavoriteUserUseCase.execute(InsertFavoriteUserUseCaseParam(favoriteUser))
            } else {
                deleteFavoriteUserByUsernameUseCase.execute(
                    DeleteFavoriteUserByUsernameUseCaseParam(
                        username.orEmpty()
                    )
                )
            }
        }
    }

    sealed interface ViewEffect {
        data class ShowData(val data: UserDetailDomainModel) : ViewEffect
        data class ShowErrorMessage(val message: String) : ViewEffect
        data class ShowProgressBar(val isVisible: Boolean) : ViewEffect
    }

    sealed interface ViewEvent {
        data class OnActivityStarted(val username: String) : ViewEvent
        data class OnFavoriteButtonClicked(
            val id: Int? = null,
            val avatarUrl: String?,
            val username: String?,
            val isFavorite: Boolean
        ) : ViewEvent
    }
}