package com.example.apairy

import android.content.Context
import androidx.room.Room
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.HiveDao
import com.example.apairy.database.HiveRepository
import com.example.apairy.database.MigrationDao
import com.example.apairy.database.MigrationRepository
import com.example.apairy.database.MistakeDao
import com.example.apairy.database.MistakeRepository
import com.example.apairy.remote.api.ApiaryApi
import com.example.apairy.repository.MyRepository
import com.example.apairy.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideSessionManager( @ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }


    @Singleton
    @Provides
    fun provideApiaryApi(): ApiaryApi{
        val httpLoggindInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggindInterceptor).build()

        return Retrofit.Builder().baseUrl("http://192.168.0.22:8080").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiaryApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ApiaryDatabase{
        return Room.databaseBuilder(
            context,
            ApiaryDatabase::class.java,
            "apiary_database"
        ).build()
    }


    @Singleton
    @Provides
    fun provideMistakeDao(db: ApiaryDatabase): MistakeDao{
        return db.mistakeDao()
    }

    @Singleton
    @Provides
    fun provideHiveDao(db: ApiaryDatabase): HiveDao {
        return db.hiveDao()
    }

    @Singleton
    @Provides
    fun provideMigrationDao(db: ApiaryDatabase): MigrationDao {
        return db.migrationDao()
    }

    @Singleton
    @Provides
    fun provideRepository(api: ApiaryApi, mistakeDao: MistakeDao, sessionManager: SessionManager): MyRepository{
        return MyRepository(api, mistakeDao, sessionManager)
    }

    @Singleton
    @Provides
    fun provideHiveRepository(hiveDao: HiveDao): HiveRepository {
        return HiveRepository(hiveDao)
    }

    @Singleton
    @Provides
    fun provideMigrationRepository(migrationDao: MigrationDao): MigrationRepository {
        return MigrationRepository(migrationDao)
    }

    @Singleton
    @Provides
    fun provideMistakeRepository(mistakeDao: MistakeDao): MistakeRepository {
        return MistakeRepository(mistakeDao)
    }

}