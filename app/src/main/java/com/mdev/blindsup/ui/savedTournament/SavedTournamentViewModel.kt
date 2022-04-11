package com.mdev.blindsup.ui.savedTournament

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.mdev.blindsup.adapter.SavedTournamentAdapter
import com.mdev.blindsup.data.TournamentData

private const val TAG = "SavedTournamentViewMode"

class SavedTournamentViewModel(app: Application) : AndroidViewModel(app) {


    var blindsList = mutableListOf<TournamentData>()
    lateinit var databaseBlinds: DatabaseReference

    val blinds = MutableLiveData<MutableList<TournamentData>>()


    init {
        val acct = GoogleSignIn.getLastSignedInAccount(app)
        databaseBlinds = FirebaseDatabase.getInstance().getReference("Users/${acct.id}/Blinds")

        loadSavedTournaments()

    }

    fun loadSavedTournaments() {
     blinds.value = mutableListOf()
        val auth = Firebase.auth
        if (auth.currentUser != null) {


            databaseBlinds.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val getData = mutableListOf<TournamentData>()
                        for (blindsSnapshot in snapshot.children) {
                            var userBlinds = blindsSnapshot.getValue(TournamentData::class.java)
                            userBlinds?.id = blindsSnapshot.key


                            if (userBlinds != null) {
                                getData.add(userBlinds)
                                blinds.value = getData


                                // println("added one $userBlinds")
                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: Error loading Data")
                }
            })

        }
    }

    fun deleteTournament(position: Int) {
//Getting the path of the session to delete
        val tournamentToDelete = blindsList[position!!].id

        //deleting the session
        if (tournamentToDelete != null) {
            databaseBlinds.child(tournamentToDelete).removeValue()
        }
        blindsList.removeAt(position)
    }
}