package com.example.finances.data.datasource.model

data class Expense(
    override val id: String,
    override val name: String,
    override val description: String,
    override val value: Double,
) : Transaction()
