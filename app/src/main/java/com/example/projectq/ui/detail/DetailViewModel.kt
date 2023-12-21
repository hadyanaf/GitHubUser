package com.example.projectq.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectq.data.util.Resource
import com.example.projectq.data.util.SingleEvent
import com.example.projectq.domain.model.UserDetailDomainModel
import com.example.projectq.domain.usecase.GetUserDetailUseCase
import com.example.projectq.domain.usecase.GetUserDetailUseCaseParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {

    private val _viewEffect: MutableLiveData<SingleEvent<ViewEffect>> = MutableLiveData()
    val viewEffect: LiveData<SingleEvent<ViewEffect>> = _viewEffect

    fun processEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnActivityStarted -> onActivityStarted(event)
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
                }
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
    }
}