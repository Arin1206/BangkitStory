package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import com.dicoding.picodiploma.loginwithanimation.di.Injection2
import com.dicoding.picodiploma.loginwithanimation.view.Maps.MapsViewModel
import com.dicoding.picodiploma.loginwithanimation.view.camera.CameraViewModel

class ViewModelFactory(
    private val repository: UserRepository2
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CameraViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context, token: String? = null): ViewModelFactory {
            synchronized(this) {
                if (INSTANCE == null) {
                    val repository = if (token != null) {
                        Injection2.provideUserRepositoryWithToken(context)
                    } else {
                        Injection2.provideUserRepository(context)
                    }
                    INSTANCE = ViewModelFactory(repository)
                }
                return INSTANCE!!
            }
        }
    }
}
