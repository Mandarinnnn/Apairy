package com.example.apairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.apairy.databinding.ActivityLoginBinding
import com.example.apairy.databinding.ActivityRegisterBinding
import com.example.apairy.models.MigrationViewModel
import com.example.apairy.models.MistakeViewModel
import com.example.apairy.models.UserViewModel
import com.example.apairy.utils.Answer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val userViewModel: UserViewModel by viewModels()
    private val mistakeViewModel: MistakeViewModel by viewModels()
    private val migrationViewModel: MigrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.login.observe(this,){ answer ->
            when(answer){
                is Answer.Success -> {

                    mistakeViewModel.getAllRemoteMistakes()
                    migrationViewModel.getAllRemoteMigrations()

                    Toast.makeText(this,"Login successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is Answer.Error -> {
                    Toast.makeText(this,answer.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Answer.Loading -> {

                }
            }
        }

        binding.btnLoginEnter.setOnClickListener{
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()
            userViewModel.login(email, password)
        }


        binding.btnLoginRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}