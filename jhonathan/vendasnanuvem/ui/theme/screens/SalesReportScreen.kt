package com.jhonathan.vendasnanuvem.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhonathan.vendasnanuvem.viewmodel.ProductViewModel
import com.jhonathan.vendasnanuvem.SaleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesReportScreen(
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel,
    onBack: () -> Unit
) {
    // Observar vendas e produtos
    val sales by saleViewModel.getAllSales().collectAsState(initial = emptyList())
    val products by productViewModel.allProducts.collectAsState(initial = emptyList())

    // Combina vendas com produtos
    val salesWithProducts = sales.mapNotNull { sale ->
        val product = products.find { it.id == sale.productId }
        if (product != null) {
            SaleReportItem(
                productName = product.name,
                quantity = sale.quantity,
                totalValue = sale.totalValue
            )
        } else null
    }

    // Calcula o valor total das vendas
    val totalSalesValue = salesWithProducts.sumOf { it.totalValue }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Vendas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Cabeçalho da tabela
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Produto", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                Text("Qtd", modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.titleSmall)
                Text("Total", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
            }

            // Linhas da tabela
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(salesWithProducts) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.productName, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        Text("${item.quantity}", modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.bodySmall)
                        Text("R$ ${"%.2f".format(item.totalValue).replace('.', ',')}", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Total das vendas
            Text(
                "Valor Total: R$ ${"%.2f".format(totalSalesValue).replace('.', ',')}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

// Item do relatório
data class SaleReportItem(
    val productName: String,
    val quantity: Int,
    val totalValue: Double
)


