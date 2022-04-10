package com.mdev.blindsup.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mdev.blindsup.R
import com.mdev.blindsup.databinding.ActivityLoginBinding
import com.mdev.blindsup.ui.home.HomeActivity
import com.mdev.blindsup.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    //making a global variable for DataBinding
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initializing the binding object to my view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.logInButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }
}