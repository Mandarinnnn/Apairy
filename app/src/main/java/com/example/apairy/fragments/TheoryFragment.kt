package com.example.apairy.fragments

import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.apairy.R
import com.example.apairy.adapter.HiveAdapter
import com.example.apairy.databinding.FragmentTheoryBinding
import com.example.apairy.models.HiveViewModel
import com.example.apairy.models.Theory


class TheoryFragment : Fragment() {

    private var _binding: FragmentTheoryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTheoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.theoryTopic1.setOnClickListener{
            val category = 0
            val direction = TheoryFragmentDirections.actionTheoryFragmentToTheoryListFragment(category)
            it.findNavController().navigate(direction)
        }
        binding.theoryTopic2.setOnClickListener{
            val category = 1
            val direction = TheoryFragmentDirections.actionTheoryFragmentToTheoryListFragment(category)
            it.findNavController().navigate(direction)
        }
        binding.theoryTopic3.setOnClickListener{
            val category = 2
            val direction = TheoryFragmentDirections.actionTheoryFragmentToTheoryListFragment(category)
            it.findNavController().navigate(direction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}