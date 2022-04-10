package com.mdev.blindsup.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mdev.blindsup.R
import com.mdev.blindsup.databinding.ActivitySignupBinding
import com.mdev.blindsup.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}