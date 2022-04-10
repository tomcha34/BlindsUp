package com.mdev.blindsup.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mdev.blindsup.R
import com.mdev.blindsup.databinding.ActivityLoginBinding
import com.mdev.blindsup.ui.home.HomeActivity
import com.mdev.blindsup.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    //making a global variable for DataBinding
    private lateinit var binding : ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initializing the binding object to my view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

//        binding.logInButton.setOnClickListener {
//            startActivity(Intent(this, HomeActivity::class.java))
//        }

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Creating an object instance of GoogleSignIn, and passing the context
        //and options as parameters.
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //The GoodleButton for login

        //When clicked it will start an intent for the user to login via Google
        binding.logInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        //Get the information for user if already signed in
        val acct = GoogleSignIn.getLastSignedInAccount(this)


        //If the user is already signed into the app, go straight to the
        //HomeFragment and pass the users name as a parameter to HomeFragment.
        if (auth.currentUser != null) {

            startActivity(Intent(this, HomeActivity::class.java))
        }


    }

    //Function to authenticate the user via Firebase
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    //Change UI according to user data.
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))

        } else {
            Toast.makeText(this, "Sign-in Failed", Toast.LENGTH_LONG).show()
        }
    }

    //Once User enters login info, the result will be checked. If login worked, the
    //firebaseAuthWithGoogle() func will be called and the users ID token will be passed
    //as a parameter.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Login", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
            }
        }
    }



    }
