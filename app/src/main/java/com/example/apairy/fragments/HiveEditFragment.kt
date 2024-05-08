package com.example.apairy.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.adapter.HiveAdapter
import com.example.apairy.adapter.HiveStateAdapter
import com.example.apairy.databinding.FragmentHiveAddBinding
import com.example.apairy.databinding.FragmentHiveEditBinding
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveState
import com.example.apairy.models.HiveViewModel
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._com_example_apairy_models_HiveViewModel_HiltModules_BindsModule
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class HiveEditFragment : Fragment(), MenuProvider,HiveStateAdapter.HiveStateLongClickListener, PopupMenu.OnMenuItemClickListener {

    private var _binding: FragmentHiveEditBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var selectedHiveState: HiveState
    private val hiveViewModel: HiveViewModel by activityViewModels()


    private lateinit var hiveStateAdapter: HiveStateAdapter
    private lateinit var currentHive: Hive

    private val args: HiveEditFragmentArgs by navArgs()

    private var menuSaved = false

    private lateinit var editMenuItem: MenuItem
    private lateinit var saveMenuItem: MenuItem
    private lateinit var editHiveView: View

//    private val imgContract = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
//        binding.ivPickHive.setImageURI(it)
//        binding.ivPickHive.visibility = View.VISIBLE
//        binding.ivPickHive.tag = it.toString()
//        it?.let{
//            requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }
//    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHiveEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        editHiveView = view


        currentHive = args.hive!!

        binding.etHiveName.setText(currentHive.name)
        binding.etHiveNote.setText(currentHive.note)
        binding.etHiveQueen.setText(currentHive.queenYear)
        binding.enHiveFrame.setText(currentHive.frameCount.toString())


        binding.swHive.setChecked(currentHive.isMarked)


        binding.etHiveQueen.setOnClickListener{
            showDatePicker(binding.etHiveQueen)
        }

        binding.rvHiveStateList.setHasFixedSize(true)
        binding.rvHiveStateList.layoutManager = LinearLayoutManager(requireContext())
        hiveStateAdapter = HiveStateAdapter(this)
        binding.rvHiveStateList.adapter = hiveStateAdapter


        hiveViewModel.getStatesForHive(currentHive.id).observe(viewLifecycleOwner,){ list ->
            list?.let{
                hiveStateAdapter.updateHiveStateList(list)
            }
        }



        binding.btnSaveHiveState.setOnClickListener{
            saveHiveState()
        }


    }



    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edit_menu, menu)
             editMenuItem = menu.findItem(R.id.action_save)
        saveMenuItem = menu.findItem(R.id.action_save)

        (activity as? MainActivity)?.allocateTitle("")

        if(menuSaved){
            editMenuItem.isVisible = false
            saveMenuItem.isVisible = true
        }
    }

    private fun updateHiveInDB(view: View){
        val title = binding.etHiveName.text.toString()
        val note = binding.etHiveNote.text.toString()
        val queen = binding.etHiveQueen.text.toString()
        val frame = binding.enHiveFrame.text.toString()

        val isMarked = binding.swHive.isChecked

        if (title.isNotEmpty() || note.isNotEmpty() || frame.isNotEmpty() || queen.isNotEmpty()){
            val hive = Hive(
                title, frame.toInt(), queen, note, isMarked, currentHive.isLocallyNew, false, currentHive.isLocallyDeleted,
                currentHive.id
            )
            hiveViewModel.updateHive(hive)
            Toast.makeText(editHiveView.context,"Улей изменен", Toast.LENGTH_SHORT).show()
                //view.findNavController().popBackStack(R.id.hiveListFragment, false)
        }else{
            Toast.makeText(editHiveView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveHiveState(){
        val strength = binding.etHiveStateStrength.text.toString().toIntOrNull()
        val honey = binding.etHiveStateHoney.text.toString().toFloatOrNull()
        val framesWithBrood = binding.etHiveStateFrameWithBrood.text.toString().toIntOrNull()

        if(strength != null && honey != null && framesWithBrood != null){
            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSSS")
            val hiveState = HiveState(
                currentHive.id, strength,framesWithBrood,honey,formatter.format(Date()),
                true, false
            )
            hiveViewModel.insertHiveState(hiveState)
        }else{
            Toast.makeText(requireContext(),"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
//            R.id.action_edit -> {
//                editMenuItem.isVisible = false
//                makeEnable()
//                // Сделать элемент "Сохранить" видимым
//                saveMenuItem.isVisible = true
//
//                true
//            }

            R.id.action_save -> {
                updateHiveInDB(editHiveView)
                saveMenuItem.isVisible = false
                // Сделать элемент "Сохранить" видимым
                editMenuItem.isVisible = true
                true
            }
            R.id.action_delete -> {
                deleteHive()
                true
            }
            else -> false
        }
    }


    private fun deleteHive(){
        hiveViewModel.deleteHive(currentHive)
        editHiveView.findNavController().popBackStack()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideBottomNavigationView()
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.showBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = "${dayOfMonth}.${month + 1}.${year}"
                editText.setText(selectedDate)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun popUpDisplay(cardView: CardView){
        val popup = PopupMenu(requireContext(), cardView)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onLongItemClicked(hiveState: HiveState, cardView: CardView) {
        selectedHiveState = hiveState
        popUpDisplay(cardView)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_hive){
            hiveViewModel.deleteHiveState(selectedHiveState)
            return true
        }
        return false
    }

}