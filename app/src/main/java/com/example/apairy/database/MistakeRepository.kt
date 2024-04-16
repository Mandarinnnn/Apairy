package com.example.apairy.database

import androidx.lifecycle.LiveData
import com.example.apairy.models.Mistake

class MistakeRepository(private val mistakeDao: MistakeDao) {


    val getAllMistakes: LiveData<List<Mistake>> = mistakeDao.getAllMistakes()


    suspend fun insert(mistake: Mistake){
        mistakeDao.insertMistake(mistake)
    }

    suspend fun delete(mistake: Mistake){
        mistakeDao.deleteMistake(mistake)
    }

    suspend fun update(mistake: Mistake){
        mistakeDao.updateMistake(mistake)
    }
}