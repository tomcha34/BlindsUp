package com.mdev.blindsup.ui.home

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeViewModel(app : Application) : AndroidViewModel(app) {

        fun logoutUser(context : Context, gso: GoogleSignInOptions) {
                Firebase.auth.signOut()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInClient.signOut()
                //using our navigation component to move screens
            }

    fun showSnackbar(view: Context) {
        Toast.makeText(view, "Logout Successful", Toast.LENGTH_SHORT).show()
    }
        }
