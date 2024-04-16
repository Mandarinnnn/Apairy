package com.example.apairy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.MigrationRepository
import com.example.apairy.database.MistakeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MistakeViewModel(application: Application): AndroidViewModel(application) {
    private val repository: MistakeRepository
    val getAllMistakes: LiveData<List<Mistake>>

    init{
        val mistakeDao = ApiaryDatabase.getDatabase(application).mistakeDao()
        repository = MistakeRepository(mistakeDao)
        getAllMistakes = repository.getAllMistakes
    }

    fun insertMistake(mistake: Mistake){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(mistake)
        }
    }

    fun updateMistake(mistake: Mistake){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(mistake)
        }
    }

    fun deleteMistake(mistake: Mistake){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(mistake)
        }
    }
}