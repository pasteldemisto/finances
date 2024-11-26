package com.example.finances.data.datasource.service

import com.example.finances.data.datasource.model.Income
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IncomeApiService {

    @GET("/finances")
    suspend fun getIncomes(): Response<List<Income>>

    @GET("/finances/{id}")
    suspend fun getIncomeById(@Path("id") id:String): Response<Income>

    @POST("/finances")
    suspend fun createIncome(@Body income: Income): ResponseBody

    @DELETE("/finances/{id}")
    suspend fun deleteIncome(@Path("id") id: String): ResponseBody

    @PUT("/finances/{id}")
    suspend fun updateIncome(@Path("id") id: String, @Body income: Income): ResponseBody
}