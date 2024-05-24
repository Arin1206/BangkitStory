package com.dicoding.picodiploma.loginwithanimation.view.Maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import kotlinx.coroutines.launch

class MapsViewModel(private val userRepository: UserRepository2) : ViewModel() {

    private val _storiesWithLocation = MutableLiveData<ListStoryResponse>()
    val storiesWithLocation: LiveData<ListStoryResponse> = _storiesWithLocation

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getStoriesWithLocation(location: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.getStoriesWithLocation(location, token)
                _storiesWithLocation.value = response
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
