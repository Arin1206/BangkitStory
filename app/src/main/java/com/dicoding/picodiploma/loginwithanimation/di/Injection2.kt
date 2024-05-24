package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.view.signup.RegisterViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection2 {

    fun provideUserRepositoryWithToken(context: Context): UserRepository2 {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig().getApiService(user.token)
        return UserRepository2.getInstance(userPreference, apiService)
    }


    fun provideUserRepository(context: Context): UserRepository2 {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiService =
            ApiConfig().getApiService("") // Token sementara kosong karena tidak ada token
        return UserRepository2.getInstance(userPreference, apiService)
    }

    fun provideMainViewModelFactory(
        context: Context,
        token: String? = null
    ): RegisterViewModelFactory {
        return if (token != null) {
            RegisterViewModelFactory(provideUserRepositoryWithToken(context))
        } else {
            RegisterViewModelFactory(provideUserRepository(context))
        }
    }

}
