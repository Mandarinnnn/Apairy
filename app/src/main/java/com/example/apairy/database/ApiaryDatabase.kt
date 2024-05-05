package com.example.apairy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveState
import com.example.apairy.models.Migration
import com.example.apairy.models.Mistake


@Database(entities = [Hive::class, HiveState::class, Migration::class, Mistake::class], version = 1, exportSchema = false)
abstract class ApiaryDatabase: RoomDatabase() {

    abstract fun hiveDao() : HiveDao
    abstract fun migrationDao() : MigrationDao
    abstract fun mistakeDao() : MistakeDao

//    companion object{
//        @Volatile
//        private var INSTANCE: ApiaryDatabase? = null
//
//        fun getDatabase(context: Context) :ApiaryDatabase{
//            val tempInstance = INSTANCE
//            if(tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    ApiaryDatabase::class.java,
//                    "apiary_database"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }

}