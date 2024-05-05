package com.example.apairy.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apairy.repository.MistakeeRepository
import com.example.apairy.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MistakeeViewModel @Inject constructor(val repository: MistakeeRepository):ViewModel(){

    fun createMistake(mistake: Mistake){
        viewModelScope.launch(Dispatchers.IO){
            repository.createMistake(mistake)
        }
    }

    fun updateMistake(mistake: Mistake){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateMistake(mistake)
        }
    }

    fun deleteMistake(mistake: Mistake){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteMistake(mistake)
        }
    }

    fun getAllRemoteMistakes(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllRemoteMistakes()
        }
    }

}