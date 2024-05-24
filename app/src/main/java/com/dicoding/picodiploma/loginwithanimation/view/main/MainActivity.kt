package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import androidx.core.util.Pair
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.view.camera.CameraActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.Maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var token: String
    private lateinit var adapter: MainAdapter

    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressBar = findViewById(R.id.progressBar)
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }


        adapter = MainAdapter { item ->

            val intent = Intent(this, DetailActivity::class.java)

            intent.putExtra("listStoryItem", item)

            val detailBinding = ActivityDetailBinding.inflate(layoutInflater)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(detailBinding.ivDetailPhoto, "detailphoto"),
                Pair(detailBinding.tvDetailName, "detailname"),
                Pair(detailBinding.tvDetailDescription, "detaildescription"),
            )


            startActivity(intent, options.toBundle())
        }



        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter


        token = intent.getStringExtra("token2") ?: ""

        viewModel.getListStory(token)
        Log.d("MainActivity", "Token: $token")

        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            val userPreference = UserPreference.getInstance(dataStore)
            userPreference.getSession().collect { userModel ->
                val tokenFromPrefs = userModel.token
                val sesi = userModel.isLogin
                if (tokenFromPrefs.isNotEmpty()) {

                    Log.d("Token", "Token sudah disimpan: pengguna login $sesi,$tokenFromPrefs")


                    if (token.isEmpty()) {
                        token = tokenFromPrefs
                    }


                    loadData()
                } else {

                    Log.d("Token", "Token belum disimpan")
                }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraActivity::class.java).apply {
                putExtra("token2", token)
            }
            startActivity(intent)
        }
        setupView()
        setupAction()

    }


    override fun onResume() {
        super.onResume()
        loadData()
    }


    private fun loadData() {

        viewModel.getListStory("$token")
        viewModel.listStory.observe(this) { listStory ->
            if (listStory != null) {
                adapter.submitData(lifecycle, listStory)


                Log.d("MainActivity", "Jumlah listStory yang diambil:")

            } else {

                Log.e("MainActivity", "Gagal memuat daftar item")
                Snackbar.make(binding.root, "Failed to load items", Snackbar.LENGTH_SHORT).show()
            }
        }
        progressBar.visibility = View.GONE
    }

    private fun setupView() {
        supportActionBar?.show()

    }

    private fun setupAction() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    Log.d("User", "Pengguna logout dengan token $token")
                    Log.d("User", "Token dihapus")
                    viewModel.logout(token)

                    true
                }

                R.id.action_location -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }

                R.id.action_setting -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                else -> false
            }
        }

    }
}