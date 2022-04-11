package com.mdev.blindsup.ui.newTournament

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.FirebaseDatabase

import com.mdev.blindsup.data.TournamentData

class NewTournamentViewModel (private val app: Application) : AndroidViewModel(app) {
  var id : String? = ""

  //A function to save a new tournament to Firebase Realtime Database
  fun createTournament(name: String, blindLength: Long, startingStack: Int, smallestChip : Int, context: Context) {

    //Get the signed in user to make a unique path for the data
    val acct = GoogleSignIn.getLastSignedInAccount(context)
    //Get a reference to the database path including thr logged in users ID.
    val  databaseBlinds = FirebaseDatabase.getInstance().getReference("Users/${acct.id}/Blinds")
    //create a unique id for the saved tournament
    id = databaseBlinds.push().key
    val blind =
      id?.let { TournamentData(it, name, blindLength, startingStack, smallestChip) }

    //Push the new tournament to the Firebase Database
    if (id != null) {
      databaseBlinds.child(id!!).setValue(blind)
    }
  }

}