package com.example.finances

import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.common.RetrofitUtil
import com.example.finances.data.datasource.IncomeDataSource
import com.example.finances.data.datasource.model.Income
import com.example.finances.ui.theme.FinancesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = RetrofitUtil.getIncomeService()
        val incomeDataSource = IncomeDataSource(apiService)
        setContent {
            FinancesTheme {
                HomeScreen(incomeDataSource)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(incomeDataSource: IncomeDataSource) {
    var showDialog by remember { mutableStateOf(false) }
    var inputValue by remember { mutableStateOf(TextFieldValue("")) }
    var inputName by remember { mutableStateOf(TextFieldValue("")) }
    var inputDescription by remember { mutableStateOf(TextFieldValue("")) }


    val coroutineScope = rememberCoroutineScope()

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
                text = "R$ 0,00",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Entradas", fontSize = 16.sp)
                    Text(
                        text = "R$ 0,00",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Saídas", fontSize = 16.sp)
                    Text(
                        text = "R$ 0,00",
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
                Button(onClick = { showDialog = true }) {
                    Text(text = "Adicionar Entrada")
                }
                Button(onClick = { }) {
                    Text(text = "Adicionar Saída")
                }
            }


            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Adicionar Entrada") },
                    text = {
                        Column {
                            TextField(
                                value = inputValue,
                                onValueChange = { inputValue = it },
                                label = { Text(text = "Valor") }
                            )
                            TextField(
                                value = inputName,
                                onValueChange = { inputName = it },
                                label = { Text(text = "Nome") }
                            )
                            TextField(
                                value = inputDescription,
                                onValueChange = { inputDescription = it },
                                label = { Text(text = "Descrição") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val income = Income(
                                        id = "123",
                                        value = inputValue.text.toDouble(),
                                        name = inputName.text,
                                        description = inputDescription.text
                                        )
                                    try {
                                        val response = incomeDataSource.createIncome(income)
                                        println("Entrada criada com sucesso: $income")
                                    } catch (e: Exception) {
                                        println("Erro ao criar entrada: ${e.message}")
                                    }
                                    showDialog = false
                                    inputValue = TextFieldValue("")
                                    inputName = TextFieldValue("")
                                    inputDescription = TextFieldValue("")
                                }

                            }
                        ) {
                            Text("Adicionar")
                        }

                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

fun addIncome(value: String) {
    println("Entrada adicionada: $value")
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewHomeScreen() {
//    FinancesTheme {
//        HomeScreen(incomeDataSource)
//    }
//}
