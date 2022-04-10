package com.mdev.blindsup.ui.login

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
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

const val RC_SIGN_IN = 123

class LoginViewModel(app: Application) : AndroidViewModel(app){
//    // Configure Google Sign In
//    lateinit var auth : FirebaseAuth
//    val gso = GoogleSignInOptions
//        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(R.string.default_web_client_id.toString())
//        .requestEmail()
//        .build()
//
//    //Creating an object instance of GoogleSignIn, and passing the context
//    //and options as parameters.
//
//    val googleSignInClient = GoogleSignIn.getClient(app, gso)
//    // Initialize Firebase Auth
//    init {
//
//        auth = Firebase.auth
//
//    }
//
//    val signInIntent = googleSignInClient.signInIntent
//
//
//    //Function to authenticate the user via Firebase
//    private fun firebaseAuthWithGoogle(idToken: String, app: Activity) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(app) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("login", "signInWithCredential:success")
//                    val user = auth.currentUser
//                    updateUI(user)
//
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("signin", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
//                }
//            }
//    }
//
//
//    //Once User enters login info, the result will be checked. If login worked, the
//    //firebaseAuthWithGoogle() func will be called and the users ID token will be passed
//    //as a parameter.
//
//    fun updateUI(account: FirebaseUser?) {
//        if (account != null) {
//            val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
//            Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()
//            findNavController().navigate(
//                LoginFragmentDirections.actionLoginFragmentToHomeFragment(
//                    acct.displayName!!
//                )
//            )
//        } else {
//            Toast.makeText(requireContext(), "Sign-in Failed", Toast.LENGTH_LONG).show()
//        }
//    }
//


}