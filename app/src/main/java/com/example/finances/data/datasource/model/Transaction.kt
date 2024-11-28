package com.example.finances.data.datasource.model

sealed class Transaction {
    abstract val id: String
    abstract val name: String
    abstract val description: String
    abstract val value: Double
}