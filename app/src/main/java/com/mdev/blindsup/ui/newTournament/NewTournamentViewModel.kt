package com.mdev.blindsup.ui.newTournament

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.FirebaseDatabase

import com.mdev.blindsup.data.TournamentData

class NewTournamentViewModel (private val app: Application) : AndroidViewModel(app) {
  var id : String? = ""

  fun createTournament(name: String, blindLength: Long, startingStack: Int, smallestChip : Int, context: Context) {

    val acct = GoogleSignIn.getLastSignedInAccount(context)
    val  databaseBlinds = FirebaseDatabase.getInstance().getReference("Users/${acct.id}/Blinds")
    id = databaseBlinds.push().key
    val blind =
      id?.let { TournamentData(it, name, blindLength, startingStack, smallestChip) }

    if (id != null) {
      databaseBlinds.child(id!!).setValue(blind)
    }
  }

}