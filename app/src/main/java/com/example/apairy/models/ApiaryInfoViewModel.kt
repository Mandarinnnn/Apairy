package com.example.apairy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.HiveRepository
import com.example.apairy.database.MigrationRepository
import com.example.apairy.repository.HiveeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiaryInfoViewModel @Inject constructor(val repository: HiveRepository): ViewModel() {
   // private val repository: HiveRepository

//    init{
//        val habitDao = ApiaryDatabase.getDatabase(application).hiveDao()
//        repository = HiveRepository(habitDao)
//    }

    private val _totalStrengthCounts = MutableLiveData<StrengthCounts>()
    val totalStrengthCounts: LiveData<StrengthCounts>
        get() = _totalStrengthCounts



    fun getTotalStrengthCounts() {
        viewModelScope.launch {
            val counts = repository.getTotalStrengthCounts()
            _totalStrengthCounts.value = counts
        }
    }


    private val _totalHoneyAmount = MutableLiveData<HoneyAmountCounts>()
    val totalHoneyAmount: LiveData<HoneyAmountCounts>
        get() = _totalHoneyAmount

    fun getTotalHoneyAmount() {
        viewModelScope.launch {
            val honeyAmountCounts = repository.getTotalHoneyAmount()
            _totalHoneyAmount.value = honeyAmountCounts
        }
    }


}