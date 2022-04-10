package com.mdev.blindsup.ui.newTournament

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mdev.blindsup.R
import com.mdev.blindsup.databinding.ActivityNewTournamentBinding
import com.mdev.blindsup.ui.tournamentRunning.TournamentRunningActivity

class NewTournamentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewTournamentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_tournament)

            val newTournamentViewModel =
                ViewModelProvider(this).get(NewTournamentViewModel::class.java)

            val validationList = arrayListOf(
                binding.blindLevelInputLayout,
                binding.nicknameInputLayout,
                binding.startingStackInputLayout,
                binding.smallestChipInputLayout
            )
            binding.createTournamentButton.setOnClickListener {

                if (binding.blindLevelEditText.text.isNullOrEmpty() || binding.nicknameEditText.text.isNullOrEmpty() ||
                    binding.startingStackEditText.text.isNullOrEmpty() || binding.smalledChipEditText.text.isNullOrEmpty()
                ) {
                    for (fields in validationList) {
                        if (fields.editText?.text.isNullOrEmpty()) {
                            fields.error = "Field must not be empty"
                        } else {
                            fields.error = null
                        }
                    }
                } else {


                    newTournamentViewModel.createTournament(
                        binding.nicknameEditText.text.toString(),
                        binding.blindLevelEditText.text.toString().toLong(),
                        binding.startingStackEditText.text.toString().toInt(),
                        binding.smalledChipEditText.text.toString().toInt(),
                        this
                    )
                    val intent = Intent(this, TournamentRunningActivity::class.java)
                    intent.putExtra("tournamentDetails", newTournamentViewModel.id)
                    startActivity(intent)
                }
            }


    }
}