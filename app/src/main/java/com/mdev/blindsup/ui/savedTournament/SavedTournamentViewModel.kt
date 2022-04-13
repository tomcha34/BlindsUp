package com.mdev.blindsup.ui.savedTournament

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.mdev.blindsup.data.TournamentData

private const val TAG = "SavedTournamentViewMode"

class SavedTournamentViewModel(app: Application) : AndroidViewModel(app) {


//    var blindsList = mutableListOf<TournamentData>()
   // create an instance of your firebase database
private var databaseBlinds: DatabaseReference
    //create Livedata to populate our saved tournaments.
    val blinds = MutableLiveData<MutableList<TournamentData>>()


    //An initializer block to set our google and database references
    init {
        val acct = GoogleSignIn.getLastSignedInAccount(app)
        //It's impossible to be in this part of the app without acct having value, so I will set acct as !!
        databaseBlinds = FirebaseDatabase.getInstance().getReference("Users/${acct!!.id}/Blinds")

        //run our load tournament function
        loadSavedTournaments()

    }

    // A function to load all the saved user tournaments from Firebase
    // and load them into a livedata list to show in our recyclerView
    private fun loadSavedTournaments() {
     //Initialize out liveData list as an empty mutable list
     blinds.value = mutableListOf()
        val auth = Firebase.auth
        //If the user is logged in, get the tournament data from firebase
        if (auth.currentUser != null) {


            //Add a listener to the database so we can iterate through all our tournaments
                // stored in Firebase Realtime Database
            databaseBlinds.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val getData = mutableListOf<TournamentData>()
                        for (blindsSnapshot in snapshot.children) {
                            val userBlinds = blindsSnapshot.getValue(TournamentData::class.java)
                            userBlinds?.id = blindsSnapshot.key

                            //Save each session for the for loop to our liveData object
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

    //A function to remove a Saved Tournament
    fun deleteTournament(position: Int) {
        //Getting the path of the session to delete
        val tournamentToDelete = blinds.value?.get(position)?.id

        //deleting the session
        if (tournamentToDelete != null) {
            databaseBlinds.child(tournamentToDelete).removeValue()
        }
        //Updating the livedata
        blinds.value?.removeAt(position)
    }
}