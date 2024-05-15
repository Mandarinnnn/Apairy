package com.example.apairy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apairy.R
import com.example.apairy.fragments.MigrationListFragmentDirections
import com.example.apairy.fragments.MistakeListFragmentDirections
import com.example.apairy.models.Mistake

class MistakeAdapter: RecyclerView.Adapter<MistakeAdapter.MistakeViewHolder>(){

    private val MistakeList = ArrayList<Mistake>()
    private val fullList = ArrayList<Mistake>()


    inner class MistakeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mistake_layout = itemView.findViewById<CardView>(R.id.mistake_card)
        val title = itemView.findViewById<TextView>(R.id.tv_mistake_title)
        val date = itemView.findViewById<TextView>(R.id.tv_mistake_year)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MistakeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MistakeViewHolder(inflater.inflate(R.layout.mistake_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MistakeViewHolder, position: Int) {
        val currentMistake = MistakeList[position]
        holder.title.text = currentMistake.name
        holder.date.text = "Год: " + currentMistake.year


        holder.mistake_layout.setOnClickListener{
            val direction = MistakeListFragmentDirections.actionMistakeListFragmentToMistakeEditFragment(currentMistake)
            it.findNavController().navigate(direction)
        }
    }



    override fun getItemCount(): Int {
        return MistakeList.size
    }



    fun updateMistakeList(newList: List<Mistake>){

        fullList.clear()
        fullList.addAll(newList)

        MistakeList.clear()
        MistakeList.addAll(fullList)
        notifyDataSetChanged()

    }

    fun filterMistakeList(search: String){
        MistakeList.clear()

        for(item in fullList){
            if(item.name?.lowercase()?.contains(search.lowercase()) == true){
                MistakeList.add(item)
            }
        }
        notifyDataSetChanged()
    }


}