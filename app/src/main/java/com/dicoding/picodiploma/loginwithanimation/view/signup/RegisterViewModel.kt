package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.api.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository2) : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse>
        get() = _registerResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val registerResponse = userRepository.register(name, email, password)
                _registerResult.value = registerResponse
            } catch (e: Exception) {
                _errorMessage.value = "Registrasi gagal: Email Already Taken"
            }
        }
    }
}
