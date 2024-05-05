package com.example.apairy.remote.api

import androidx.room.Delete
import com.example.apairy.remote.models.RemoteHive
import com.example.apairy.remote.models.RemoteHiveState
import com.example.apairy.remote.models.RemoteMigration
import com.example.apairy.remote.models.RemoteMistake
import com.example.apairy.remote.models.Response
import com.example.apairy.remote.models.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiaryApi {

    @Headers("Content-Type: application/json")
    @POST("/users/register")
    suspend fun register(
        @Body user: User
    ): Response


    @Headers("Content-Type: application/json")
    @POST("/users/login")
    suspend fun login(
        @Body user: User
    ): Response






    @Headers("Content-Type: application/json")
    @POST("/mistakes/create")
    suspend fun createMistake(
        @Header("Authorization") token: String,
        @Body remoteMistake: RemoteMistake
    ): Response


    @Headers("Content-Type: application/json")
    @GET("/mistakes")
    suspend fun getAllMistakes(
        @Header("Authorization") token: String,
    ): List<RemoteMistake>


    @Headers("Content-Type: application/json")
    @POST("/mistakes/update")
    suspend fun updateMistake(
        @Header("Authorization") token: String,
        @Body remoteMistake: RemoteMistake
    ): Response


    @Headers("Content-Type: application/json")
    @DELETE("/mistakes/delete")
    suspend fun deleteMistake(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): Response












//migration


    @Headers("Content-Type: application/json")
    @POST("/migration/create")
    suspend fun createMigration(
        @Header("Authorization") token: String,
        @Body remoteMigration: RemoteMigration
    ): Response


    @Headers("Content-Type: application/json")
    @GET("/migrations")
    suspend fun getAllMigration(
        @Header("Authorization") token: String,
    ): List<RemoteMigration>


    @Headers("Content-Type: application/json")
    @POST("/migration/update")
    suspend fun updateMigration(
        @Header("Authorization") token: String,
        @Body remoteMigration: RemoteMigration
    ): Response


    @Headers("Content-Type: application/json")
    @DELETE("/migration/delete")
    suspend fun deleteMigration(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): Response



    //hive

    @Headers("Content-Type: application/json")
    @POST("/hive/create")
    suspend fun createHive(
        @Header("Authorization") token: String,
        @Body remoteHive: RemoteHive
    ): Response


    @Headers("Content-Type: application/json")
    @GET("/hives")
    suspend fun getAllHives(
        @Header("Authorization") token: String,
    ): List<RemoteHive>


    @Headers("Content-Type: application/json")
    @POST("/hive/update")
    suspend fun updateHive(
        @Header("Authorization") token: String,
        @Body remoteHive: RemoteHive
    ): Response


    @Headers("Content-Type: application/json")
    @DELETE("/hive/delete")
    suspend fun deleteHive(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): Response


    //hive state

    @Headers("Content-Type: application/json")
    @POST("/hive-state/create")
    suspend fun createHiveState(
        @Header("Authorization") token: String,
        @Body remoteHiveState: RemoteHiveState
    ): Response

    @Headers("Content-Type: application/json")
    @GET("/hive-states")
    suspend fun getAllHiveStates(
        @Header("Authorization") token: String,
    ): List<RemoteHiveState>

    @Headers("Content-Type: application/json")
    @DELETE("/hive-state/delete")
    suspend fun deleteHiveState(
        @Header("Authorization") token: String,
        @Query("id") id: String,
        @Query("hiveId") hiveId: String,
    ): Response

}