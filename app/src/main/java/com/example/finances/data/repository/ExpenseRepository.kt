package com.example.finances.data.repository

import com.example.finances.data.datasource.ExpenseDataSource
import com.example.finances.data.datasource.model.Expense
import com.example.finances.ui.model.ExpenseModel
import okhttp3.ResponseBody

class ExpenseRepository(
    private val expenseApiDataSource: ExpenseDataSource = ExpenseDataSource()
) {

    suspend fun getExpenseById(id: String): ExpenseModel {
        val income = expenseApiDataSource.getExpenseById(id).getOrThrow()
        return ExpenseModel(income.id, income.name, income.description, income.value)
    }

    suspend fun getExpenses(): List<ExpenseModel> {
        return expenseApiDataSource.getExpenses().getOrThrow()
            .map {
                ExpenseModel(it.id, it.name, it.description, it.value)
            }
    }

    suspend fun createExpense(expense: Expense): ResponseBody {
        return expenseApiDataSource.createExpense(expense)
    }

    suspend fun deleteExpense(id: String): ResponseBody {
        return expenseApiDataSource.deleteExpense(id)
    }

    suspend fun updateExpense(id: String, expense: Expense): ResponseBody {
        return expenseApiDataSource.updateExpense(id, expense)
    }

}