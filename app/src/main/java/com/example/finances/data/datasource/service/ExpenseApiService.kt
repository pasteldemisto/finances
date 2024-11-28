package com.example.finances.data.datasource.service

import com.example.finances.data.datasource.model.Expense
import com.example.finances.data.datasource.model.Income
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExpenseApiService {

    @GET("/expenses")
    suspend fun getExpenses(): Response<List<Expense>>

    @GET("/expenses/{id}")
    suspend fun getExpenseById(@Path("id") id:String): Response<Expense>

    @POST("/expenses")
    suspend fun createExpense(@Body expense: Expense): ResponseBody

    @DELETE("/expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String): ResponseBody

    @PUT("/expenses/{id}")
    suspend fun updateExpense(@Path("id") id: String, @Body expense: Expense): ResponseBody

}