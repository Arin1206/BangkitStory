package com.dicoding.picodiploma.loginwithanimation.data.pref

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.NewStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.view.paging3.StoryPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository2 private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService

) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        try {
            val registerResponse = apiService.register(name, email, password)
            return registerResponse
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        try {
            val loginResponse = apiService.login(email, password)
            return loginResponse
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getListStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, "Bearer $token")
            }
        ).liveData
    }


    suspend fun uploadImage(
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part,
        lon: RequestBody?,
        lat: RequestBody?
    ): NewStoryResponse {
        try {
            return apiService.uploadImage(description, photo, lon, lat, "Bearer $token")
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getStoriesWithLocation(location: Int, token: String): ListStoryResponse {
        try {
            return apiService.getStoriesWithLocation(location, "Bearer $token")
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun logout(token: String) {
        userPreference.logout(token)
    }

    companion object {
        @Volatile
        private var instance: UserRepository2? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository2 =
            instance ?: synchronized(this) {
                instance ?: UserRepository2(userPreference, apiService)
            }.also { instance = it }
    }
}