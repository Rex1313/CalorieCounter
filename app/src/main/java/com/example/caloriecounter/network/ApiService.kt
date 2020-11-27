package com.example.caloriecounter.network

import com.example.caloriecounter.database.Entry
import com.example.caloriecounter.models.CreateUserResponse
import com.example.caloriecounter.models.User
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("createuser")
    suspend fun createUser(@Body user: User): CreateUserResponse

    @POST("updateentry")
    suspend fun uploadEntry(@Header("token") token:String, @Body it: Entry) {

    }

}