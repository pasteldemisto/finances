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

    @GET("/incomes")
    suspend fun getIncomes(): Response<List<Income>>

    @GET("/incomes/{id}")
    suspend fun getIncomeById(@Path("id") id:String): Response<Income>

    @POST("/incomes")
    suspend fun createIncome(@Body income: Income): ResponseBody

    @DELETE("/incomes/{id}")
    suspend fun deleteIncome(@Path("id") id: String): ResponseBody

    @PUT("/incomes/{id}")
    suspend fun updateIncome(@Path("id") id: String, @Body income: Income): ResponseBody
}