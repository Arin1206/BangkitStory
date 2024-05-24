package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.api.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository2) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse>
        get() = _loginResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val loginResponse = userRepository.login(email, password)
                if (loginResponse.error == false && loginResponse.loginResult != null) {
                    val token = loginResponse.loginResult.token ?: ""
                    saveSession(UserModel(email, token))
                }
                _loginResult.value = loginResponse
            } catch (e: Exception) {
                _errorMessage.value = "Registrasi gagal: ${e.message}"
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}