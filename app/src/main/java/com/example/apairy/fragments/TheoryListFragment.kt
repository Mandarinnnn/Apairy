package com.example.apairy.fragments

import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apairy.R
import com.example.apairy.adapter.TheoryAdapter
import com.example.apairy.databinding.FragmentTheoryBinding
import com.example.apairy.databinding.FragmentTheoryListBinding
import com.example.apairy.models.HiveViewModel
import com.example.apairy.models.Theory


class TheoryListFragment : Fragment() {
    lateinit var adapter: TheoryAdapter

    private var _binding: FragmentTheoryListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: TheoryListFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTheoryListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.rvTheoryList.hasFixedSize()
        binding.rvTheoryList.layoutManager = LinearLayoutManager(requireContext())

        adapter = TheoryAdapter()

        binding.rvTheoryList.adapter = adapter

        when(args.category){
            0-> adapter.updateTheoryList(fillArrays(resources.getStringArray(R.array.titles_topic1),
                getImageId(R.array.images_topic1),0))
            1-> adapter.updateTheoryList(fillArrays(resources.getStringArray(R.array.titles_topic2),
                getImageId(R.array.images_topic2),1))
            2->adapter.updateTheoryList(fillArrays(resources.getStringArray(R.array.titles_topic3),
                getImageId(R.array.images_topic3),2))
        }


    }




    fun fillArrays(titleArray:Array<String>, imageIdArray: Array<Int>, category: Int):List<Theory>
    {
        var TheoryArray = ArrayList<Theory>()
        for(n in 0..titleArray.size - 1)
        {
            var listItem = Theory(imageIdArray[n],titleArray[n], category)
            TheoryArray.add(listItem)
        }
        return TheoryArray
    }


    fun getImageId(imageArrayId: Int):Array<Int> {
        val imageArray: TypedArray = resources.obtainTypedArray(imageArrayId)

        val size = imageArray.length()


        val imgIdArray = Array(size) { i -> imageArray.getResourceId(i,0) }

        imageArray.recycle()
        return imgIdArray
    }




}