package com.example.apairy.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.apairy.MainActivity
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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ApiaryInfoFragment : Fragment() {
    private var _binding: FragmentApiaryInfoBinding? = null
    private val binding get() = _binding!!
    //lateinit var apiaryInfoViewModel: ApiaryInfoViewModel
    private val apiaryInfoViewModel: ApiaryInfoViewModel by activityViewModels()

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


        apiaryInfoViewModel.totalStrengthCounts.observe(viewLifecycleOwner, Observer { counts ->

            val list: ArrayList<PieEntry> = ArrayList()
            list.add(PieEntry(counts.strongCount.toFloat(), "Сильные"))
            list.add(PieEntry(counts.mediumCount.toFloat(), "Средние"))
            list.add(PieEntry(counts.weakCount.toFloat(), "Слабые"))

            val pieDataSet = PieDataSet(list, "")

            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)

            pieDataSet.valueTextSize = 15f
            pieDataSet.valueTextColor = Color.BLACK

            val pieData = PieData(pieDataSet)

            binding.pieChart.data = pieData

            binding.pieChart.description.text = ""

            binding.pieChart.centerText = "Сила ульев"
            binding.pieChart.invalidate()

        })


//
        apiaryInfoViewModel.getTotalStrengthCounts()


        apiaryInfoViewModel.totalHoneyAmount.observe(viewLifecycleOwner, Observer { count ->

            binding.tvInfoHoneyAmount.text = "Общее количество мёда: " + count.toString()+ " кг."

        })
        apiaryInfoViewModel.getTotalHoneyAmount()



        apiaryInfoViewModel.getAllHives.observe(viewLifecycleOwner,){ list ->
            binding.tvInfoHiveCount.text = "Общее количество ульев: "+ list.size.toString()
        }
    }

}