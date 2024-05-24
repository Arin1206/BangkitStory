package com.dicoding.picodiploma.loginwithanimation.view.camera

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CameraViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CameraViewModel

    @Mock
    private lateinit var repository: UserRepository2

    @Mock
    private lateinit var toastObserver: Observer<String>

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        viewModel = CameraViewModel(repository)
        viewModel.toastMessage.observeForever(toastObserver)
    }

    @After
    fun tearDown() {
        viewModel.toastMessage.removeObserver(toastObserver)
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test uploadImage success`() = runBlockingTest {
        val token = "your_token"
        val description = "Image description".toRequestBody("text/plain".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData(
            "photo",
            "image.jpg",
        )
        val lonRequestBody = "123.45".toRequestBody("text/plain".toMediaTypeOrNull())
        val latRequestBody = "67.89".toRequestBody("text/plain".toMediaTypeOrNull())


        viewModel.uploadImage(token, description, photoPart, lonRequestBody, latRequestBody)


        Mockito.verify(repository)
            .uploadImage(token, description, photoPart, lonRequestBody, latRequestBody)
        Mockito.verifyNoMoreInteractions(repository)
        Mockito.verify(toastObserver, Mockito.never()).onChanged(Mockito.anyString())
    }

    @Test
    fun `test uploadImage failure`() = runBlockingTest {
        // Given
        val token = "your_token"
        val description = "Image description".toRequestBody("text/plain".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData(
            "photo",
            "image.jpg",
        )
        val lonRequestBody = "123.45".toRequestBody("text/plain".toMediaTypeOrNull())
        val latRequestBody = "67.89".toRequestBody("text/plain".toMediaTypeOrNull())
        Mockito.`when`(
            repository.uploadImage(
                token,
                description,
                photoPart,
                lonRequestBody,
                latRequestBody
            )
        )
            .thenThrow(RuntimeException("Upload failed"))


        viewModel.uploadImage(token, description, photoPart, lonRequestBody, latRequestBody)


        val latch = CountDownLatch(1)
        val observer = Observer<String> {
            if (it == "Upload failed") {
                latch.countDown()
            }
        }
        viewModel.toastMessage.observeForever(observer)
        assertTrue(latch.await(2, TimeUnit.SECONDS))

        Mockito.verify(repository)
            .uploadImage(token, description, photoPart, lonRequestBody, latRequestBody)
        Mockito.verify(toastObserver).onChanged("Upload failed")
        Mockito.verifyNoMoreInteractions(repository)

        viewModel.toastMessage.removeObserver(observer)
    }

}