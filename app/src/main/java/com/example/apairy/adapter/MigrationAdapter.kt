package com.example.apairy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.apairy.R
import com.example.apairy.databinding.HiveListItemBinding
import com.example.apairy.fragments.HiveListFragmentDirections
import com.example.apairy.fragments.MigrationListFragmentDirections
import com.example.apairy.models.Migration


class MigrationAdapter: RecyclerView.Adapter<MigrationAdapter.MigrationViewHolder>() {

    private val MigrationList = ArrayList<Migration>()
    private val fullList = ArrayList<Migration>()


    inner class MigrationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val migration_layout = itemView.findViewById<CardView>(R.id.migration_card)
        val title = itemView.findViewById<TextView>(R.id.tv_migration_title)
        val hiveCount = itemView.findViewById<TextView>(R.id.tv_migration_hive_count)
        val date = itemView.findViewById<TextView>(R.id.tv_migration_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MigrationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MigrationViewHolder(inflater.inflate(R.layout.migration_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MigrationViewHolder, position: Int) {
        val currentMigration = MigrationList[position]
        holder.title.text = currentMigration.name
        //holder.title.isSelected = true
        holder.hiveCount.text = currentMigration.hiveCount.toString()
        holder.date.text = currentMigration.startDate
        //holder.date.isSelected = true


        holder.migration_layout.setOnClickListener{
            val direction = MigrationListFragmentDirections.actionMigrationListFragmentToMigrationEditFragment(currentMigration)
            it.findNavController().navigate(direction)
        }
    }


    override fun getItemCount(): Int {
        return MigrationList.size
    }



    fun updateMigrationList(newList: List<Migration>){

        fullList.clear()
        fullList.addAll(newList)

        MigrationList.clear()
        MigrationList.addAll(fullList)
        notifyDataSetChanged()

    }

    fun filterMigrationList(search: String){
        MigrationList.clear()

        for(item in fullList){
            if(item.name?.lowercase()?.contains(search.lowercase()) == true){
                MigrationList.add(item)
            }
        }
        notifyDataSetChanged()
    }


}