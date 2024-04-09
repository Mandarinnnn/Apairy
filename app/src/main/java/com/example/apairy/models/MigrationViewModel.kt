package com.example.apairy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.MigrationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MigrationViewModel(application: Application): AndroidViewModel(application) {
    private val repository: MigrationRepository
    val getAllMigrations: LiveData<List<Migration>>

    init{
        val migrationDao = ApiaryDatabase.getDatabase(application).migrationDao()
        repository = MigrationRepository(migrationDao)
        getAllMigrations = repository.getAllMigrations
    }

    fun insertMigration(migrations: Migration){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(migrations)
        }
    }

    fun updateMigration(migrations: Migration){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(migrations)
        }
    }

    fun deleteMigration(migrations: Migration){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(migrations)
        }
    }
}