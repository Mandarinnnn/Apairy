package com.example.apairy.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.apairy.R
import com.example.apairy.databinding.FragmentApiaryInfoBinding
import com.example.apairy.databinding.FragmentHiveAddBinding
import com.example.apairy.models.ApiaryInfoViewModel
import com.example.apairy.models.HiveViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.color.MaterialColors


class ApiaryInfoFragment : Fragment() {
    private var _binding: FragmentApiaryInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiaryInfoViewModel: ApiaryInfoViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApiaryInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiaryInfoViewModel = ViewModelProvider(this).get(ApiaryInfoViewModel::class.java)


       // val list: ArrayList<PieEntry> = ArrayList()

        apiaryInfoViewModel.totalStrengthCounts.observe(viewLifecycleOwner, Observer { counts ->

            val list: ArrayList<PieEntry> = ArrayList()
            list.add(PieEntry(counts.strongCount.toFloat(), "Сильные"))
            list.add(PieEntry(counts.mediumCount.toFloat(), "Средние"))
            list.add(PieEntry(counts.weakCount.toFloat(), "Слабые"))

            val pieDataSet = PieDataSet(list, "list")

            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)

            pieDataSet.valueTextSize = 15f
            pieDataSet.valueTextColor = Color.BLACK

            val pieData = PieData(pieDataSet)

            binding.pieChart.data = pieData

            binding.pieChart.description.text = "Pie"

            binding.pieChart.centerText = "list"
            binding.pieChart.invalidate()

        })


//
        apiaryInfoViewModel.getTotalStrengthCounts()


        apiaryInfoViewModel.totalHoneyAmount.observe(viewLifecycleOwner, Observer { counts ->

            val list: ArrayList<BarEntry> = ArrayList()
            list.add(BarEntry(1f, counts.honeyFrom1To3.toFloat()))
            list.add(BarEntry(2f, counts.honeyFrom3To5.toFloat()))
            list.add(BarEntry(3f, counts.honeyFrom5To7.toFloat()))
            list.add(BarEntry(4f, counts.honeyFrom7To9.toFloat()))
            list.add(BarEntry(5f, counts.honeyFrom9ToMore.toFloat()))

            val barDataSet = BarDataSet(list, "list")

            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)

            barDataSet.valueTextSize = 15f
            barDataSet.valueTextColor = Color.BLACK

            val barData = BarData(barDataSet)

            binding.barChart.data = barData
            //binding.barChart.setFitBars(true)

            binding.barChart.description.text = "Bar"

            binding.barChart.invalidate()

        })
        apiaryInfoViewModel.getTotalHoneyAmount()
    }

}