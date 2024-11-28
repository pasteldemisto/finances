package com.example.finances.common

import com.example.finances.data.datasource.service.ExpenseApiService
import com.example.finances.data.datasource.service.IncomeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {

    fun getIncomeService(): IncomeApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(IncomeApiService::class.java)
    }

    fun getExpenseService(): ExpenseApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ExpenseApiService::class.java)
    }

}