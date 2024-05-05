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
import com.example.apairy.models.Hive


class HiveAdapter: RecyclerView.Adapter<HiveAdapter.HiveViewHolder>() {

    private val HiveList = ArrayList<Hive>()
    private val fullList = ArrayList<Hive>()


    inner class HiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val hive_layout = itemView.findViewById<CardView>(R.id.hive_card)
        val title = itemView.findViewById<TextView>(R.id.tv_hive_title)
        val strength = itemView.findViewById<TextView>(R.id.tv_hive_strength)
        val date = itemView.findViewById<TextView>(R.id.tv_hive_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiveViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HiveViewHolder(inflater.inflate(R.layout.hive_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return HiveList.size
    }

    override fun onBindViewHolder(holder: HiveViewHolder, position: Int) {
        val currentHive = HiveList[position]
        holder.title.text = currentHive.name
        holder.title.isSelected = true
        holder.strength.text = currentHive.frameCount.toString()
        holder.date.text = currentHive.queenYear
        holder.date.isSelected = true


        holder.hive_layout.setOnClickListener{



            val direction = HiveListFragmentDirections.actionHiveListFragmentToHiveEditFragment(currentHive)
            it.findNavController().navigate(direction)


            //listener.onItemClicked(HiveList[holder.adapterPosition])
        }

//        holder.hive_layout.setOnLongClickListener{
//            listener.onLongItemClicked(HiveList[holder.adapterPosition], holder.hive_layout)
//            true
//        }
    }

    fun updateHiveList(newList: List<Hive>){

        fullList.clear()
        fullList.addAll(newList)

        HiveList.clear()
        HiveList.addAll(fullList)
        notifyDataSetChanged()

    }

    fun filterHiveList(search: String){
        HiveList.clear()

        for(item in fullList){
            if(item.name?.lowercase()?.contains(search.lowercase()) == true){
                HiveList.add(item)
            }


        }
        notifyDataSetChanged()
    }

    interface HiveClickListener{
        fun onItemClicked(hive: Hive)
        fun onLongItemClicked(hive: Hive,cardView: CardView)
    }


}