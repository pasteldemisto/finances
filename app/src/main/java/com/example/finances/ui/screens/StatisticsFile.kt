package com.example.finances.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finances.data.datasource.ExpenseDataSource
import com.example.finances.data.datasource.IncomeDataSource
import kotlinx.coroutines.launch

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun StatisticsScreen(
        incomeDataSource: IncomeDataSource,
        expenseDataSource: ExpenseDataSource,
        navController: NavController
) {
    var totalIncomes by remember { mutableStateOf(0.0) }
    var totalExpenses by remember { mutableStateOf(0.0) }
    var total by remember { mutableStateOf(0.0) }
    val coroutineScope = rememberCoroutineScope()

    suspend fun updateTotals() {
        val resultIncome = incomeDataSource.getIncomes()
        resultIncome.onSuccess { incomes ->
            totalIncomes = incomes.sumOf { it.value }
        }.onFailure { exception ->
            println("erro ao buscar entrada: ${exception.message}")
        }
        val resultExpense = expenseDataSource.getExpenses()
        resultExpense.onSuccess { expenses ->
            totalExpenses = expenses.sumOf { it.value }
        }.onFailure { exception ->
            println("erro ao buscar entrada: ${exception.message}")
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            updateTotals()
            total = totalIncomes + totalExpenses
        }
    }

    val incomePercentage = if (total > 0) (totalIncomes / total * 100).toFloat() else 0f
    val expensePercentage = if (total > 0) (totalExpenses / total * 100).toFloat() else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Estatísticas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Total de Entradas: R$ %.2f".format(totalIncomes))
        Text("Total de Saídas: R$ %.2f".format(totalExpenses))
        Text("Saldo Final: R$ %.2f".format(totalIncomes - totalExpenses))

        Spacer(modifier = Modifier.height(32.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            val canvasSize = size.minDimension
            val radius = canvasSize / 2
            val rect = Rect(0f, 0f, canvasSize, canvasSize)
            var startAngle = 0f

            val incomeSweepAngle = (incomePercentage / 100) * 360
            drawArc(
                color = Color.Green,
                startAngle = startAngle,
                sweepAngle = incomeSweepAngle,
                useCenter = true,
                topLeft = rect.topLeft,
                size = rect.size
            )
            startAngle += incomeSweepAngle

            val expenseSweepAngle = (expensePercentage / 100) * 360
            drawArc(
                color = Color.Red,
                startAngle = startAngle,
                sweepAngle = expenseSweepAngle,
                useCenter = true,
                topLeft = rect.topLeft,
                size = rect.size
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}