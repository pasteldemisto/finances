package com.example.finances.data.datasource

import com.example.finances.common.RetrofitUtil
import com.example.finances.data.datasource.model.Expense
import com.example.finances.data.datasource.model.Income
import com.example.finances.data.datasource.service.ExpenseApiService
import okhttp3.ResponseBody

class ExpenseDataSource(
    private val expenseApiService: ExpenseApiService = RetrofitUtil.getExpenseService()
) {

    suspend fun getExpenses(): Result<List<Expense>> {
        val response = expenseApiService.getExpenses()
        if(response.isSuccessful) {
            return Result.success(response.body()!!)
        }
        return Result.failure(Exception("Error to get all tasks from remote service"))
    }

    suspend fun getExpenseById(id: String): Result<Expense> {
        val response = expenseApiService.getExpenseById(id)
        if(response.isSuccessful) {
            return  Result.success(response.body()!!)
        }
        return Result.failure(Exception("Error to get a task by id"))
    }

    suspend fun createExpense(expense: Expense): ResponseBody {
        val response = expenseApiService.createExpense(expense);
        return response
    }

    suspend fun deleteExpense(id: String): ResponseBody {
        val response = expenseApiService.deleteExpense(id);
        return response
    }

    suspend fun updateExpense(id: String, expense: Expense): ResponseBody {
        val response = expenseApiService.updateExpense(id, expense);
        return response
    }


}