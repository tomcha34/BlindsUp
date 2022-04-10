package com.mdev.blindsup.ui.newTournament

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.mdev.blindsup.data.TournamentData

class NewTournamentViewModel (private val app: Application) : AndroidViewModel(app) {

  fun createTournament(name: String, blindLength: Long, startingStack: Int, smallestChip : Int) {
    val tournament = TournamentData(name, blindLength, startingStack, smallestChip)

  }

}