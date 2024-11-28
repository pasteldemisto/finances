 package com.example.finances

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*

import com.example.finances.common.RetrofitUtil

import com.example.finances.ui.theme.FinancesTheme
import com.example.finances.ui.navigation.MainNavHost


 class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            FinancesTheme {
                MainNavHost()
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewHomeScreen() {
//    FinancesTheme {
//        HomeScreen(incomeDataSource)
//    }
//}
