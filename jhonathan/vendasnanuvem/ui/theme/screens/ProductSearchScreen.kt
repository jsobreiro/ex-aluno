package com.jhonathan.vendasnanuvem.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhonathan.vendasnanuvem.data.model.Product
import com.jhonathan.vendasnanuvem.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchScreen(
    productViewModel: ProductViewModel,
    onEditProduct: (Int) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var searchedProducts by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Coleta todos os produtos para pesquisa
    val products by productViewModel.allProducts.collectAsState(initial = emptyList())

    // Função para formatar valores como moeda
    fun formatCurrency(value: Double): String {
        return "R$ ${"%.2f".format(value).replace('.', ',')}"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesquisar Produtos") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barra de pesquisa
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Pesquisar por nome ou descrição") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    searchedProducts = products.filter { product ->
                        product.name.contains(query, ignoreCase = true) ||
                                product.description.contains(query, ignoreCase = true)
                    }
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Pesquisar") // Apenas o ícone de lupa
                }
            }

            // Lista de resultados da pesquisa
            if (searchedProducts.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(searchedProducts) { product ->
                        ProductSearchItem(
                            product = product,
                            onClick = { onEditProduct(product.id) }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum produto encontrado. Faça uma pesquisa.")
                }
            }
        }
    }
}

@Composable
fun ProductSearchItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = product.name, style = MaterialTheme.typography.titleMedium)
            Text(text = product.description, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Quantidade: ${product.quantity}")
            Text("Preço: ${"R$ %.2f".format(product.value).replace('.', ',')}") // Formatação aplicada
        }
    }
}
