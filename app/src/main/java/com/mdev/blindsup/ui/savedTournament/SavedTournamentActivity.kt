package com.mdev.blindsup.ui.savedTournament

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.mdev.blindsup.R
import com.mdev.blindsup.adapter.SavedTournamentAdapter
import com.mdev.blindsup.data.Constants
import com.mdev.blindsup.databinding.ActivitySavedTournamentBinding
import com.mdev.blindsup.ui.newTournament.NewTournamentActivity
import com.mdev.blindsup.ui.tournamentRunning.TournamentRunningActivity

class SavedTournamentActivity : AppCompatActivity(), SavedTournamentAdapter.OnItemClickListener {
    private lateinit var binding: ActivitySavedTournamentBinding
    val adapter = SavedTournamentAdapter(this, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_tournament)

        val savedViewModel = ViewModelProvider(this).get(SavedTournamentViewModel::class.java)
        binding.recyclerview.adapter = adapter
        adapter.setBlindData(savedViewModel.blinds.value!!)
        savedViewModel.blinds.observe(this, {
            adapter.setBlindData(it)
        })




        binding.floatingActionButton.setOnClickListener {
        startActivity(Intent(this, NewTournamentActivity::class.java))

        }
    }

    override fun onItemClick(position: Int) {
//        val builder: AlertDialog.Builder =
//            AlertDialog.Builder(this)
//        builder.setTitle("Delete Session")
//        builder.setMessage("Are you sure you want to delete this session? This cannot be undone")
//
//        builder.setPositiveButton(
//            Html.fromHtml("<font color='#FFFFFF'>Yes</font>")
//        ) { dialog, which -> // Do nothing but close the dialog
//            val position = viewHolder?.adapterPosition
//
//            //Getting the path of the session to delete
//            val sessionToDelete = sessionList[position!!].id
//
//            //deleting the session
//            if (sessionToDelete != null) {
//                databasePoker.child(sessionToDelete).removeValue()
//            }
//            sessionList.removeAt(position)
//            recyclerView.adapter!!.notifyItemRemoved(position)
//            val snack = Snackbar.make(fabButton, "Session successfully deleted", Snackbar.LENGTH_SHORT)
//            snack.setBackgroundTint(resources.getColor(com.google.firebase.database.R.color.black))
//            snack.setTextColor(resources.getColor(com.google.firebase.database.R.color.white))
//            snack.show()
//
//
//            dialog.dismiss()
//
//        }
//
//        builder.setOnCancelListener {
//            val position = viewHolder?.adapterPosition
//            if (position != null) {
//                recyclerView.adapter!!.notifyItemChanged(position)
//            }
//        }
//
//        builder.setNegativeButton(
//            Html.fromHtml("<font color='#FFFFFF'>NO</font>"),
//            DialogInterface.OnClickListener { dialog, which ->
//                val position = viewHolder?.adapterPosition
//                if (position != null) {
//                    recyclerView.adapter!!.notifyItemChanged(position)
//                }
//
//                dialog.dismiss()
//            })
//
//        val alert: AlertDialog = builder.create()
//        alert.show()
        val intent = Intent(this, TournamentRunningActivity::class.java)
        intent.putExtra(Constants().SAVED_SELECTION, position)
        startActivity(intent)
    }
}