package com.zenenta.helper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tisto.helpers.extension.logs
import com.tisto.helpers.extension.setImagePicasso
import com.zenenta.helper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            ivImage.setImagePicasso("urlasal aja"){
                logs("ini nanti error")
            }
        }
    }
}