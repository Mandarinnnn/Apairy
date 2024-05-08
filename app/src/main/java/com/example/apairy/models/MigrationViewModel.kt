package com.example.apairy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.HiveRepository
import com.example.apairy.database.MigrationRepository
import com.example.apairy.repository.MigrationnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MigrationViewModel @Inject constructor(val repository: MigrationnRepository): ViewModel() {
   // private val repository: MigrationRepository
    val getAllMigrations: LiveData<List<Migration>> = repository.getAllMigrations

//    init{
//        val migrationDao = ApiaryDatabase.getDatabase(application).migrationDao()
//        repository = MigrationRepository(migrationDao)
//        getAllMigrations = repository.getAllMigrations
//    }

    fun insertMigration(migrations: Migration){
        viewModelScope.launch(Dispatchers.IO){
            repository.createMigration(migrations)
        }
    }

    fun updateMigration(migrations: Migration){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateMigration(migrations)
        }
    }

    fun deleteMigration(migrations: Migration){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteMigration(migrations)
        }
    }


    fun syncMigrations(){
        viewModelScope.launch(Dispatchers.IO){
            repository.syncMigrations()
        }
    }

    fun getAllRemoteMigrations(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllRemoteMigrations()
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }
}