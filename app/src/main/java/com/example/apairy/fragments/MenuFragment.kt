package com.example.apairy.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.apairy.LoginActivity
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.databinding.FragmentMenuBinding
import com.example.apairy.databinding.FragmentMigrationAddBinding
import com.example.apairy.models.HiveViewModel
import com.example.apairy.models.MigrationViewModel
import com.example.apairy.models.MistakeViewModel
import com.example.apairy.utils.Answer
import com.example.apairy.utils.SessionManager


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!


    private val hiveViewModel: HiveViewModel by activityViewModels()
    private val migrationViewModel: MigrationViewModel by activityViewModels()
    private val mistakeViewModel: MistakeViewModel by activityViewModels()

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        binding.tvEmail.text = sessionManager.getEmail()

        binding.llMigration.setOnClickListener{
            findNavController().navigate(R.id.action_menuFragment_to_migrationListFragment)
        }

        binding.llMistake.setOnClickListener{
            findNavController().navigate(R.id.action_menuFragment_to_mistakeListFragment)
        }

        binding.llLogout.setOnClickListener{
            showLogoutDialog()
        }


        binding.llSync.setOnClickListener{
            mistakeViewModel.getSyncState()
        }

        mistakeViewModel.syncState.observe(requireActivity(),){ answer ->
            when(answer){
                is Answer.Success -> {

                    migrationViewModel.syncMigrations()
                    hiveViewModel.syncHives()
                    hiveViewModel.syncHiveStates()
                    mistakeViewModel.syncMistakes()

                    mistakeViewModel.clearSyncState()
                    Toast.makeText(requireContext(),"Синхронизация прошла", Toast.LENGTH_SHORT).show()
                }
                is Answer.Error -> {
                    mistakeViewModel.clearSyncState()
                    Toast.makeText(requireContext(),"Не получилось синхронизировать", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }


    private fun showLogoutDialog() {

        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Выход из аккаунта")
            .setMessage("Вы точно хотите выйти из аккаунта?")


        dialogBuilder.setPositiveButton("Да") { dialog, which ->
            sessionManager.logout()

            migrationViewModel.deleteAll()
            hiveViewModel.deleteAll()
            mistakeViewModel.deleteAll()


            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        dialogBuilder.setNegativeButton("Отмена") { dialog, which ->

        }
        dialogBuilder.show()
    }

}