package com.example.projectq.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectq.data.local.model.ProductDomain
import com.example.projectq.data.util.Resource
import com.example.projectq.data.util.SingleEvent
import com.example.projectq.domain.usecase.GetListProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getListProductUseCase: GetListProductUseCase
) : ViewModel() {

    private val _viewEffect: MutableLiveData<SingleEvent<ViewEffect>> = MutableLiveData()
    val viewEffect: LiveData<SingleEvent<ViewEffect>> = _viewEffect

    fun processEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnActivityStarted -> onActivityStarted()
            is ViewEvent.OnCardClicked -> onCardClicked(event)
        }
    }

    private fun onActivityStarted() {
        viewModelScope.launch {
            when (val response = getListProductUseCase.execute()) {
                is Resource.Error -> {
                    _viewEffect.value =
                        SingleEvent(ViewEffect.ShowErrorMessage(response.message.orEmpty()))
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    _viewEffect.value =
                        SingleEvent(ViewEffect.ShowData(response.data?.productResponses.orEmpty()))
                }
            }
        }
    }

    private fun onCardClicked(event: ViewEvent.OnCardClicked) {

    }

    sealed interface ViewEffect {
        data class ShowData(val data: List<ProductDomain>) : ViewEffect
        data class ShowErrorMessage(val message: String) : ViewEffect
    }

    sealed interface ViewEvent {
        object OnActivityStarted : ViewEvent
        data class OnCardClicked(val id: Int) : ViewEvent
    }
}