package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.di.Injection2
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: RegisterViewModel
    private lateinit var myButton: MyRegister
    private lateinit var myEditText: MyEdit
    private lateinit var myEditEmail: MyEmail
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButton = findViewById(R.id.my_register)
        progressBar = findViewById(R.id.progressBar)
        myEditText = findViewById(R.id.ed_register_password)
        myEditEmail = findViewById(R.id.ed_register_email)

        myEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        myEditEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        myButton.setOnClickListener {
            Toast.makeText(
                this@SignupActivity,
                myEditText.text,
                Toast.LENGTH_SHORT
            ).show()
        }
        myButton.setOnClickListener {
            Toast.makeText(
                this@SignupActivity,
                myEditEmail.text,
                Toast.LENGTH_SHORT
            ).show()
        }


        setMyButtonEnable()
        setupView()
        setupAction()


    }

    private fun setMyButtonEnable() {
        val password = myEditText.text?.toString()
        val email = myEditEmail.text?.toString()
        val isPasswordValid = password != null && password.length >= 8
        val isEmailValid =
            email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        myButton.isEnabled = isPasswordValid && isEmailValid
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

    private fun isEmailUnique(email: String): Boolean {
        return true
    }

    private fun setupAction() {
        val viewModelFactory = Injection2.provideMainViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

        binding.myRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            progressBar.visibility = View.VISIBLE
            if (isEmailUnique(email)) {
                viewModel.register(name, email, password)

            } else {

                Toast.makeText(this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }

        }


        viewModel.registerResult.observe(this, Observer { registerResponse ->

            registerResponse?.let {

                if (registerResponse.error == false) {

                    Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Registrasi gagal: ${registerResponse.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressBar.visibility = View.GONE
            }
        })



        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {

                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}