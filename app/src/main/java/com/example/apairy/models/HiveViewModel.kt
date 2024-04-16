package com.example.apairy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.HiveRepository
import com.example.apairy.models.relations.HiveWithStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HiveViewModel(application: Application): AndroidViewModel(application) {
    private val repository: HiveRepository
    val getAllHives: LiveData<List<Hive>>

    init{
        val habitDao = ApiaryDatabase.getDatabase(application).hiveDao()
        repository = HiveRepository(habitDao)
        getAllHives = repository.getAllHives
    }

    fun insertHive(hive: Hive){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(hive)
        }
    }

    fun updateHive(hive: Hive){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(hive)
        }
    }

    fun deleteHive(hive: Hive){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(hive)
        }
    }

    fun insertHiveState(hiveState: HiveState){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(hiveState)
        }
    }

    fun deleteHiveState(hiveState: HiveState){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(hiveState)
        }
    }

    fun getStatesForHive(hiveId: Int): LiveData<List<HiveState>> {
        return repository.getStatesForHive(hiveId)
    }
}