package com.example.finances.data.repository

import com.example.finances.data.datasource.IncomeDataSource
import com.example.finances.data.datasource.model.Income
import com.example.finances.ui.model.IncomeModel
import okhttp3.ResponseBody

class IncomeRepository (
    private val incomeApiDataSource: IncomeDataSource = IncomeDataSource()
) {

    suspend fun getIncomeById(id: String): IncomeModel {
        val income = incomeApiDataSource.getIncomeById(id).getOrThrow()
        return IncomeModel(income.id, income.name, income.description, income.value)
    }

    suspend fun getIncomes(): List<IncomeModel> {
        return incomeApiDataSource.getIncomes().getOrThrow()
            .map {
                IncomeModel(it.id, it.name, it.description, it.value)
            }
    }

    suspend fun createIncome(income: Income): ResponseBody {
        return incomeApiDataSource.createIncome(income)
    }

    suspend fun deleteTask(id: String): ResponseBody {
        return incomeApiDataSource.deleteIncome(id)
    }

    suspend fun updateTask(id: String, income: Income): ResponseBody {
        return incomeApiDataSource.updateIncome(id, income)
    }

}