package com.example.finances.ui.screens

import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import android.annotation.SuppressLint
import android.icu.number.NumberFormatter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.common.RetrofitUtil
import com.example.finances.data.datasource.ExpenseDataSource
import com.example.finances.data.datasource.IncomeDataSource
import com.example.finances.data.datasource.model.Expense
import com.example.finances.data.datasource.model.Income
import com.example.finances.data.datasource.model.Transaction
import com.example.finances.ui.theme.FinancesTheme
import kotlinx.coroutines.CoroutineScope
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(incomeDataSource: IncomeDataSource, expenseDataSource: ExpenseDataSource) {

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var incomeList by remember { mutableStateOf<List<Income>>(emptyList()) }
    var expenseList by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var totalIncomes by remember { mutableStateOf(0.0) }
    var totalExpenses by remember { mutableStateOf(0.0) }
    var combinedList by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }

    val coroutineScope = rememberCoroutineScope()

    suspend fun updateTotals() {
        val resultIncome = incomeDataSource.getIncomes()
        resultIncome.onSuccess { incomes ->
            incomeList = incomes
            totalIncomes = incomes.sumOf { it.value }
        }.onFailure { exception ->
            println("erro ao buscar entrada: ${exception.message}")
        }
        val resultExpense = expenseDataSource.getExpenses()
        resultExpense.onSuccess { expenses ->
            expenseList = expenses
            totalExpenses = expenses.sumOf { it.value }
        }.onFailure { exception ->
            println("erro ao buscar entrada: ${exception.message}")
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            updateTotals()
            combinedList = incomeList + expenseList
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Finanças Pessoais",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Saldo Atual",
                fontSize = 18.sp
            )
            Text(
                text = "R$ ${"%.2f".format(totalIncomes - totalExpenses)}",
                fontSize = 32.sp,
                color = if (totalIncomes - totalExpenses >= 0)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Entradas", fontSize = 16.sp)
                    Text(
                        text = "R$ ${"%.2f".format(totalIncomes)}",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Saídas", fontSize = 16.sp)
                    Text(
                        text = "R$ ${"%.2f".format(totalExpenses)}",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Button(onClick = { showAddDialog = true }) {
                    Text(text = "Adicionar Entrada")
                }
                Button(onClick = { }) {
                    Text(text = "Adicionar Saída")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Display the list of incomes
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                combinedList.forEach { transaction ->
                    println("transacao: ${transaction.name}")
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = transaction.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Descrição: ${transaction.description}",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Valor: R$ ${transaction.value}",
                                    fontSize = 14.sp,
                                    color = when (transaction) {
                                        is Income -> MaterialTheme.colorScheme.primary
                                        is Expense -> MaterialTheme.colorScheme.error
                                    }
                                )
                                Text(
                                    text = when (transaction) {
                                        is Income -> "Tipo: Entrada"
                                        is Expense -> "Tipo: Saída"
                                    },
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(1.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            deleteTransaction(transaction, incomeDataSource, expenseDataSource)
                                            combinedList = updateCombinedList(incomeDataSource, expenseDataSource)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Excluir",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            showEditDialog = true
                                            transactionToEdit = transaction
//                                            combinedList = updateCombinedList(incomeDataSource, expenseDataSource)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                    }
                }
            }

            if (showEditDialog && transactionToEdit != null) {
                EditTransactionDialog(
                    transaction = transactionToEdit!!,
                    onDismiss = {
                        showEditDialog = false
                        transactionToEdit = null
                    },
                    onSave = { updatedTransaction ->
                        coroutineScope.launch {
                            try {
                                if (updatedTransaction is Income) {
                                    incomeDataSource.updateIncome(updatedTransaction.id, updatedTransaction)
                                } else if (updatedTransaction is Expense) {
                                    expenseDataSource.updateExpense(updatedTransaction.id, updatedTransaction)
                                }
                                combinedList = updateCombinedList(incomeDataSource, expenseDataSource)
                                updateTotals()
                                println("transação atualizada com sucesso!")
                            } catch (e: Exception) {
                                println("erro ao atualizar transação: ${e.message}")
                            } finally {
                                showEditDialog = false
                                transactionToEdit = null
                            }
                        }
                    }
                )
            }

            if (showAddDialog) {
                AddIncomeDialog(
                    onDismiss = { showAddDialog = false },
                    onSave = { addedTransaction ->
                        coroutineScope.launch {
                            try {
                                if (addedTransaction is Income) {
                                    incomeDataSource.createIncome(addedTransaction)
                                } else if (addedTransaction is Expense) {
                                    expenseDataSource.createExpense(addedTransaction)
                                }
                                combinedList = updateCombinedList(incomeDataSource, expenseDataSource)
                                updateTotals()
                                println("transação adicionada com sucesso!")
                            } catch (e: Exception) {
                                println("erro ao atualizar transação: ${e.message}")
                            } finally {
                                showAddDialog = false
                            }
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun AddIncomeDialog(
    onDismiss: () -> Unit,
    onSave: (Transaction) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Adicionar Entrada") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") }
                )
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val income = Income(
                    id = Random.nextInt(0, 1001).toString(),
                    value = value.toDouble(),
                    name = name,
                    description = description
                )

                onSave(income)
                onDismiss()
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EditTransactionDialog(
    transaction: Transaction,
    onDismiss: () -> Unit,
    onSave: (Transaction) -> Unit,
) {
    var name by remember { mutableStateOf(transaction.name) }
    var description by remember { mutableStateOf(transaction.description) }
    var value by remember { mutableStateOf(transaction.value.toString()) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Editar Transação") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") }
                )
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedTransaction = if (transaction is Income) {
                    Income(transaction.id, name, description, value.toDouble())
                } else {
                    Expense(transaction.id, name, description, value.toDouble())
                }
                onSave(updatedTransaction)
                onDismiss()
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

suspend fun deleteTransaction(
    transaction: Transaction,
    incomeDataSource: IncomeDataSource,
    expenseDataSource: ExpenseDataSource
) {
    try {
        when (transaction) {
            is Income -> incomeDataSource.deleteIncome(transaction.id)
            is Expense -> expenseDataSource.deleteExpense(transaction.id)
        }
        println("item excluído")
    } catch (e: Exception) {
        println("erro ao excluir: ${e.message}")
    }
}

suspend fun updateCombinedList(
    incomeDataSource: IncomeDataSource,
    expenseDataSource: ExpenseDataSource
) : List<Transaction> {
    var incomeList: List<Income> = listOf()
    var expenseList: List<Expense> = listOf()

    val resultIncome = incomeDataSource.getIncomes()
    resultIncome.onSuccess { incomes ->
        incomeList = incomes
    }.onFailure { exception ->
        println("erro ao buscar entrada: ${exception.message}")
    }
    val resultExpense = expenseDataSource.getExpenses()
    resultExpense.onSuccess { expenses ->
        expenseList = expenses
    }.onFailure { exception ->
        println("erro ao buscar entrada: ${exception.message}")
    }
    return incomeList + expenseList
}



//@Preview(showBackground = true)
//@Composable
//fun PreviewHomeScreen() {
//    FinancesTheme {
//        HomeScreen(incomeDataSource)
//    }
//}
