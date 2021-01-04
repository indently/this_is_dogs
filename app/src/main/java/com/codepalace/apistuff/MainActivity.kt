package com.codepalace.apistuff

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.codepalace.apistuff.api.ApiRequest
import com.codepalace.apistuff.api.BASE_URL
import com.codepalace.apistuff.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        backgroundAnimation()

        makeApiRequest()

        binding.apiRequestButton.setOnClickListener {
            makeApiRequest()
        }
    }

    private fun backgroundAnimation() {
        val animatonDrawable: AnimationDrawable = binding.rlLayout.background as AnimationDrawable
        animatonDrawable.apply {
            setEnterFadeDuration(1000)
            setExitFadeDuration(3000)
            start()
        }
    }

    private fun makeApiRequest() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getRandomDog()
                Log.d("Main", "Size: ${response.fileSizeBytes}")

                withContext(Dispatchers.Main) {
                    Glide.with(applicationContext).load(response.url).into(binding.ivRandomDog)
                }

            } catch (e: Exception) {
                Log.e("Main", "Error: ${e.message}")
            }

        }
    }
}