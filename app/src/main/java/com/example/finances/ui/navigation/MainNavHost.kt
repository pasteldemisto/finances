package com.example.finances.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finances.common.RetrofitUtil
import com.example.finances.ui.screens.HomeScreen
import com.example.finances.ui.screens.LoginScreen
import com.example.finances.data.datasource.ExpenseDataSource
import com.example.finances.data.datasource.IncomeDataSource

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val apiIncomeService = RetrofitUtil.getIncomeService()
    val apiExpenseService = RetrofitUtil.getExpenseService()
    val incomeDataSource = IncomeDataSource(apiIncomeService)
    val expenseDataSource = ExpenseDataSource(apiExpenseService)

    NavHost(navController = navController, startDestination = "home") {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("home") {
            HomeScreen(incomeDataSource, expenseDataSource)
        }
    }
}