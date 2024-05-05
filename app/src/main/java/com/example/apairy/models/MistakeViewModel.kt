package com.example.apairy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.MigrationRepository
import com.example.apairy.database.MistakeRepository
import com.example.apairy.repository.MistakeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MistakeViewModel @Inject constructor(val repository: MistakeeRepository): ViewModel() {
    //private val repository: MistakeRepository
    val getAllMistakes: LiveData<List<Mistake>> = repository.getAllMistakes

//    init{
//        val mistakeDao = ApiaryDatabase.getDatabase(application).mistakeDao()
//        repository = MistakeRepository(mistakeDao)
//        getAllMistakes = repository.getAllMistakes
//    }

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

    fun syncMistakes(){
        viewModelScope.launch(Dispatchers.IO){
            repository.syncMistakes()
        }
    }

    fun getAllRemoteMistakes(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllRemoteMistakes()
        }
    }

//    fun deleteMistake(mistake: Mistake){
//        viewModelScope.launch(Dispatchers.IO){
//            repository.delete(mistake)
//        }
//    }
}