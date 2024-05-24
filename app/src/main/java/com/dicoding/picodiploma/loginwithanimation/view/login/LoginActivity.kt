package com.dicoding.picodiploma.loginwithanimation.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        LoginViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var tokenFromPrefs: String
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = findViewById(R.id.progressBar)
        setupView()
        setupAction()
    }

    private fun saveUserDataToStorage(email: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            UserPreference.getInstance(dataStore).saveSession(UserModel(email, token, true))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {

        binding.loginButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password)
        }

        viewModel.loginResult.observe(this, Observer { loginResponse ->

            if (loginResponse.error == false) {
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                val token = loginResponse.loginResult?.token
                if (token != null) {
                    saveUserDataToStorage(email, token)

                    lifecycleScope.launch {
                        val userPreference = UserPreference.getInstance(dataStore)
                        userPreference.getSession().collect { userModel ->
                            tokenFromPrefs = userModel.token
                            val sesi = userModel.isLogin
                            if (tokenFromPrefs.isNotEmpty()) {

                                Log.d(
                                    "Token",
                                    "Token sudah disimpan: pengguna login $sesi,$tokenFromPrefs"
                                )
                            } else {

                                Log.d("Token", "Token belum disimpan")
                            }
                        }
                    }


                    val intent = Intent(this, MainActivity::class.java)

                    intent.putExtra("token2", tokenFromPrefs)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Token null", Toast.LENGTH_SHORT).show()
                }
            } else {

                Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {

                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
