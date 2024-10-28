package com.zenenta.helper

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.tisto.helpers.extension.formatCurrency
import com.tisto.helpers.extension.getDouble
import com.tisto.helpers.extension.imagePicker
import com.tisto.helpers.extension.logs
import com.tisto.helpers.extension.onChangeRupiah
import com.tisto.helpers.extension.pushActivity
import com.tisto.helpers.extension.setImagePicasso
import com.tisto.helpers.extension.showConfirmDialog
import com.tisto.helpers.extension.toRupiah
import com.zenenta.helper.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvExample.setOnClickListener {
                showConfirmDialog("Token expired!",
                    "Sesi login telah habis",
                    actionText = "Login",
                    actionTextSecondary = "Tutup Aplikasi",
                    onAction = {
                    },
                    onActionSecondary = {

                    })
            }
        }

    }

    private val profileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                val fileImage = File(uri.path!!)
                binding.ivImage.setImageURI(uri)
                logs(fileImage.path)
                logs(fileImage.name)
            }
        }
}