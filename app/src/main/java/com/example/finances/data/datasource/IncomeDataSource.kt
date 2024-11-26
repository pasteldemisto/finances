package com.example.finances.data.datasource

import com.example.finances.common.RetrofitUtil
import com.example.finances.data.datasource.model.Income
import com.example.finances.data.datasource.service.IncomeApiService
import okhttp3.ResponseBody

class IncomeDataSource(
    private val incomeApiService: IncomeApiService = RetrofitUtil.getIncomeService()
) {

    suspend fun getIncomes(): Result<List<Income>> {
        val response = incomeApiService.getIncomes()
        if(response.isSuccessful) {
            return Result.success(response.body()!!)
        }
        return Result.failure(Exception("Error to get all tasks from remote service"))
    }

    suspend fun getIncomeById(id: String): Result<Income> {
        val response = incomeApiService.getIncomeById(id)
        if(response.isSuccessful) {
            return  Result.success(response.body()!!)
        }
        return Result.failure(Exception("Error to get a task by id"))
    }

    suspend fun createIncome(income: Income): ResponseBody {
        val response = incomeApiService.createIncome(income);
        return response
    }

    suspend fun deleteIncome(id: String): ResponseBody {
        val response = incomeApiService.deleteIncome(id);
        return response
    }

    suspend fun updateIncome(id: String, income: Income): ResponseBody {
        val response = incomeApiService.updateIncome(id, income);
        return response
    }
}