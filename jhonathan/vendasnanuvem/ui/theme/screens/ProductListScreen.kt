package com.jhonathan.vendasnanuvem.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhonathan.vendasnanuvem.data.model.Product
import com.jhonathan.vendasnanuvem.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    viewModel: ProductViewModel,
    onAddProduct: () -> Unit,
    onEditProduct: (Int) -> Unit
) {
    val products by viewModel.allProducts.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProduct) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Produto")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onClick = { onEditProduct(product.id) }, // Navegar para edição
                    onDelete = { viewModel.delete(product) }
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    // Função para formatar o valor como moeda
    fun formatCurrency(value: Double): String {
        return "R$ ${"%.2f".format(value).replace('.', ',')}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Quantidade: ${product.quantity}", style = MaterialTheme.typography.bodySmall)
            // Formata o valor corretamente
            Text(text = "Valor: ${formatCurrency(product.value)}", style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Excluir Produto")
        }
    }
}
