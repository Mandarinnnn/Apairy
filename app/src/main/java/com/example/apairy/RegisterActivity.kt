package com.example.apairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.apairy.databinding.ActivityMainBinding
import com.example.apairy.databinding.ActivityRegisterBinding
import com.example.apairy.models.MigrationViewModel
import com.example.apairy.models.UserViewModel
import com.example.apairy.utils.Answer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)




        userViewModel.register.observe(this,){ answer ->
            when(answer){
                is Answer.Success -> {

                    Toast.makeText(this,"Register successfully", Toast.LENGTH_SHORT).show()
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

        binding.btnRegister.setOnClickListener{
            val email = binding.etRegisterEmail.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            userViewModel.register(email, password, confirmPassword)
        }
    }
}