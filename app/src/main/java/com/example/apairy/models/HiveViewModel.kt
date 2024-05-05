package com.example.apairy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.HiveRepository
import com.example.apairy.models.relations.HiveWithStates
import com.example.apairy.repository.HiveeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HiveViewModel @Inject constructor(val repository: HiveeRepository): ViewModel() {
    //private val repository: HiveRepository
    val getAllHives: LiveData<List<Hive>> = repository.getAllHives

//    init{
//        val habitDao = ApiaryDatabase.getDatabase(application).hiveDao()
//        repository = HiveRepository(habitDao)
//        getAllHives = repository.getAllHives
//    }

    fun insertHive(hive: Hive){
        viewModelScope.launch(Dispatchers.IO){
            repository.createHive(hive)
        }
    }

    fun updateHive(hive: Hive){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateHive(hive)
        }
    }

    fun deleteHive(hive: Hive){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteHive(hive)
        }
    }

    fun insertHiveState(hiveState: HiveState){
        viewModelScope.launch(Dispatchers.IO){
            repository.createHiveState(hiveState)
        }
    }

    fun deleteHiveState(hiveState: HiveState){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteHiveState(hiveState)
        }
    }

    fun getStatesForHive(hiveId: String): LiveData<List<HiveState>> {
        return repository.getStatesForHive(hiveId)
    }










    fun syncHives(){
        viewModelScope.launch(Dispatchers.IO){
            repository.syncHives()
        }
    }

    fun getAllRemoteHives(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllRemoteHives()
        }
    }

    fun syncHiveStates(){
        viewModelScope.launch(Dispatchers.IO){
            repository.syncHiveStates()
        }
    }

    fun getAllRemoteStates(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllRemoteHiveStates()
        }
    }



}