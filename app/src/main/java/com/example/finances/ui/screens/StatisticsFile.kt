package com.example.finances.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsScreen(totalIncomes: Double, totalExpenses: Double) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Estatísticas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text("Total de Entradas: R$ %.2f".format(totalIncomes))
        Text("Total de Saídas: R$ %.2f".format(totalExpenses))
        Text("Saldo Final: R$ %.2f".format(totalIncomes - totalExpenses))
    }
}