package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository2) : ViewModel() {
    private lateinit var _listStory: LiveData<PagingData<ListStoryItem>>
    val listStory: LiveData<PagingData<ListStoryItem>>
        get() = _listStory

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getListStory(token: String) {
        viewModelScope.launch {
            try {
                // Use the PagingData LiveData returned by getListStory
                _listStory = repository.getListStory(token)
            } catch (e: Exception) {
                _error.value = "Failed to fetch stories: ${e.message}"
            }
        }
    }


    fun logout(token: String) {
        viewModelScope.launch {
            repository.logout(token)
        }
    }


}
