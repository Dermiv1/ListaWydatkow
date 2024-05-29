package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseTracker()
                }
            }
        }
    }
}

@Composable
fun ExpenseTracker() {

    fun getItemColor(index: Int): Color {
        return if (index % 2 == 0) Color.LightGray else Color.White
    }

    val expenseList = remember { mutableStateListOf<Expense>() }
    var expenseName by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var expenseCategory by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text(text = "Podaj nazwę wydatku.") },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text(text = "Podaj kwotę wydatku.") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box {
            Button(onClick = { isExpanded = true }) {
                Text(text = "Kategoria wydatku")
            }
            DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                DropdownMenuItem(text = { Text("Jedzenie") }, onClick = {
                    expenseCategory = "Jedzenie"
                    isExpanded = false
                })
                DropdownMenuItem(text = { Text("Transport") }, onClick = {
                    expenseCategory = "Transport"
                    isExpanded = false
                })
                DropdownMenuItem(text = { Text("Inne") }, onClick = {
                    expenseCategory = "Inne"
                    isExpanded = false
                })
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (expenseName.isNotEmpty() && amountText.isNotEmpty() && expenseCategory.isNotEmpty()) {
                expenseList.add(
                    Expense(
                        name = expenseName,
                        amount = amountText.toDouble(),
                        category = expenseCategory
                    )
                )
                expenseName = ""
                amountText = ""
                expenseCategory = ""
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text(text = "Dodaj")
        }

        if (expenseList.isEmpty()) {
            Text(
                text = "Pusta lista",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                itemsIndexed(expenseList) { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(getItemColor(index)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.name}, ${item.amount} PLN, ${item.category}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        )
                        IconButton(onClick = { expenseList.remove(item) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }

        val totalAmount = expenseList.sumOf { it.amount }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Suma wydatków: $totalAmount PLN",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseTrackerPreview() {
    ExpenseTrackerTheme {
        ExpenseTracker()
    }
}
