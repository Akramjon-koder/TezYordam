package com.iteach.tezyordam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iteach.tezyordam.databinding.ActivityLoginBinding
import com.iteach.tezyordam.databinding.ActivityMainBinding
import com.iteach.tezyordam.utils.LoginPref

class LoginActivity : AppCompatActivity() {
    var _binding: ActivityLoginBinding? = null
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.send.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        //if(LoginPref.ReadLogin())
    }
}