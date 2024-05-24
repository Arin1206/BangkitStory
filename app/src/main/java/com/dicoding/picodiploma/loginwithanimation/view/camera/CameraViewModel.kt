package com.dicoding.picodiploma.loginwithanimation.view.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CameraViewModel(private val repository: UserRepository2) : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun uploadImage(token: String, description: RequestBody, photo: MultipartBody.Part, lon: RequestBody? = null, lat: RequestBody? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.uploadImage(token, description, photo, lon, lat)
            } catch (e: Exception) {
                _toastMessage.postValue(e.message ?: "Upload failed")
            }
        }
    }
}
