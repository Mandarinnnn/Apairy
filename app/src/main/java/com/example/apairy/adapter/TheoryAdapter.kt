package com.example.apairy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apairy.R
import com.example.apairy.fragments.HiveListFragmentDirections
import com.example.apairy.fragments.TheoryFragmentDirections
import com.example.apairy.fragments.TheoryListFragmentDirections
import com.example.apairy.models.Theory


class TheoryAdapter: RecyclerView.Adapter<TheoryAdapter.TheoryViewHolder>() {


    private val theoryList = ArrayList<Theory>()

    class TheoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val theory_layout = itemView.findViewById<ConstraintLayout>(R.id.theory_layout)
        val image = itemView.findViewById<ImageView>(R.id.iv_theory_image)
        val title = itemView.findViewById<TextView>(R.id.tv_theory_title)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TheoryViewHolder(inflater.inflate(R.layout.theory_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return theoryList.size
    }

    override fun onBindViewHolder(holder: TheoryViewHolder, position: Int) {
        val theory = theoryList[position]

        holder.image.setImageResource(theory.image_id)
        holder.title.text = theory.title



        holder.theory_layout.setOnClickListener{
            val direction = TheoryListFragmentDirections.actionTheoryListFragmentToTheoryContentFragment(theory.category, position)
            it.findNavController().navigate(direction)
        }
    }


    fun updateTheoryList(newList: List<Theory>){
        theoryList.clear()
        theoryList.addAll(newList)
        notifyDataSetChanged()
    }

}