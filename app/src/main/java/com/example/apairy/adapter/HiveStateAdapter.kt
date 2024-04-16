package com.example.apairy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apairy.R
import com.example.apairy.fragments.HiveListFragmentDirections
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveState

class HiveStateAdapter: RecyclerView.Adapter<HiveStateAdapter.HiveStateViewHolder>() {
    private val HiveStateList = ArrayList<HiveState>()
    private val fullList = ArrayList<HiveState>()


    inner class HiveStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hiveStateLayout = itemView.findViewById<CardView>(R.id.hive_state_card)
        val date = itemView.findViewById<TextView>(R.id.tv_hive_state_date)
        val strength = itemView.findViewById<TextView>(R.id.tv_hive_state_strength)
        val honey = itemView.findViewById<TextView>(R.id.tv_hive_state_honey)
        val frameWithBrood = itemView.findViewById<TextView>(R.id.tv_hive_state_frame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiveStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HiveStateViewHolder(inflater.inflate(R.layout.hive_state_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return HiveStateList.size
    }

    override fun onBindViewHolder(holder: HiveStateViewHolder, position: Int) {
        val currentHiveState = HiveStateList[position]
        holder.date.text = currentHiveState.date
        holder.strength.text = "сила:   " + currentHiveState.strength.toString()
        holder.honey.text = "мед:    " + currentHiveState.honey.toString()
        holder.frameWithBrood.text = "расп.:  " + currentHiveState.framesWithBrood.toString()
    }

    fun updateHiveStateList(newList: List<HiveState>) {
        fullList.clear()
        fullList.addAll(newList)

        HiveStateList.clear()
        HiveStateList.addAll(fullList)
        notifyDataSetChanged()
    }
}