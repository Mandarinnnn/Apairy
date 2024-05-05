package com.example.apairy.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apairy.database.MistakeRepository
import com.example.apairy.remote.models.User
import com.example.apairy.repository.MyRepository
import com.example.apairy.utils.Answer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class UserViewModel @Inject constructor(val repository: MyRepository):ViewModel() {


    private val _register = MutableLiveData<Answer<String>>()
    val register: LiveData<Answer<String>> = _register

    private val _login = MutableLiveData<Answer<String>>()
    val login: LiveData<Answer<String>> = _login


    fun register(email: String, password: String, confirmPassword: String){
        viewModelScope.launch(Dispatchers.IO){

            if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                _register.postValue(Answer.Error("Some Fields Are Empty"))
                return@launch
            }

            if(!isEmailValid(email)){
                _register.postValue(Answer.Error("Email Isn't Correct"))
                return@launch
            }

            if(!isPasswordValid(password)){
                _register.postValue(Answer.Error("Password has to be in 4 to 30 length"))
                return@launch
            }
            if(password != confirmPassword){
                _register.postValue(Answer.Error("Password And ConfirmPassword Aren't the same"))
                return@launch
            }

            _register.postValue(repository.register(User(email, password)))
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO){

            if(email.isEmpty() || password.isEmpty()){
                _login.postValue(Answer.Error("Some Fields Are Empty"))
                return@launch
            }

            if(!isEmailValid(email)){
                _login.postValue(Answer.Error("Email Isn't Correct"))
                return@launch
            }

            if(!isPasswordValid(password)){
                _login.postValue(Answer.Error("Password has to be in 4 to 30 length"))
                return@launch
            }
            _login.postValue(repository.login(User(email, password)))
        }
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO){
            repository.logout()
        }
    }



    fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun isPasswordValid(password: String): Boolean {
        val minLength = 4
        val maxLength = 30
        return password.length in minLength..maxLength
    }

}