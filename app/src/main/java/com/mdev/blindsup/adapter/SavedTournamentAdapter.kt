package com.mdev.blindsup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mdev.blindsup.R
import com.mdev.blindsup.data.TournamentData

class SavedTournamentAdapter(
    private val context: Context,

    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SavedTournamentAdapter.ItemViewHolder>() {
    var blindsData = mutableListOf<TournamentData>()
    fun setBlindData(blinds: List<TournamentData>) {
        this.blindsData = blinds.toMutableList()
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val nicknameTextView: TextView = view.findViewById(R.id.tournamentNickname)
        val blindLevelsTextView: TextView = view.findViewById(R.id.blindLevels)
        val startingStackTextView: TextView = view.findViewById(R.id.startingStack)
        val smallestChipTextView: TextView = view.findViewById(R.id.smallestChip)


        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = blindsData[position]
//        if (holder.adapterPosition > lastPosition) {
//            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_two)
//            holder.itemView.startAnimation(animation)
//            lastPosition = holder.adapterPosition
//        }
        holder.nicknameTextView.text = item.name
        holder.blindLevelsTextView.text = "Blinds Length: ${item.blindLength}"
        holder.startingStackTextView.text = "Starting Stack: ${item.startingStack}"
        holder.smallestChipTextView.text = "Smallest Chip: ${item.smallestChip}"

    }

    override fun getItemCount(): Int {
        return blindsData.size
    }
}