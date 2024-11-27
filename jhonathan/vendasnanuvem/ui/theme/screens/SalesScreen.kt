package com.jhonathan.vendasnanuvem.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jhonathan.vendasnanuvem.data.model.Product
import com.jhonathan.vendasnanuvem.data.model.Sale
import com.jhonathan.vendasnanuvem.viewmodel.ProductViewModel
import com.jhonathan.vendasnanuvem.SaleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel,
    navController: NavController // Aceitando NavController diretamente
) {
    var query by remember { mutableStateOf("") }
    var searchedProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var cart by remember { mutableStateOf<List<Sale>>(emptyList()) }
    var quantityToBuy by remember { mutableStateOf(1) }

    // Função para formatar valores como moeda
    fun formatCurrency(value: Double): String {
        return "R$ ${"%.2f".format(value).replace('.', ',')}"
    }

    val totalValue = cart.sumOf { it.totalValue }
    val products by productViewModel.allProducts.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vendas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    label = { Text("Pesquisar produto") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    searchedProducts = products.filter { product ->
                        product.name.contains(query, ignoreCase = true) ||
                                product.description.contains(query, ignoreCase = true)
                    }
                }) {
                    Text("Pesquisar")
                }
            }

            // Resultados da pesquisa
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(searchedProducts) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = product.name, fontSize = 16.sp)
                            Text(text = "Estoque: ${product.quantity}", fontSize = 14.sp)
                            Text(text = "Preço: ${formatCurrency(product.value)}", fontSize = 14.sp)
                            OutlinedTextField(
                                value = quantityToBuy.toString(),
                                onValueChange = {
                                    quantityToBuy = it.toIntOrNull() ?: 1
                                },
                                label = { Text("Quantidade") },
                                modifier = Modifier.width(120.dp)
                            )
                        }
                        Button(onClick = {
                            if (quantityToBuy > 0 && quantityToBuy <= product.quantity) {
                                val sale = Sale(
                                    productId = product.id,
                                    quantity = quantityToBuy,
                                    totalValue = product.value * quantityToBuy
                                )
                                cart = cart + sale
                                searchedProducts = searchedProducts.filter { it.id != product.id }
                            }
                        }) {
                            Text("Adicionar")
                        }
                    }
                }
            }

            // Carrinho
            if (cart.isNotEmpty()) {
                Text("Carrinho", fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cart) { sale ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val product = products.find { it.id == sale.productId }
                            Text(product?.name ?: "Produto", fontSize = 16.sp)
                            Text("Qtd: ${sale.quantity}", fontSize = 16.sp)
                            Text("Total: ${formatCurrency(sale.totalValue)}", fontSize = 16.sp)
                        }
                    }
                }
                Text(
                    "Valor Total: ${formatCurrency(totalValue)}",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(
                    onClick = {
                        saleViewModel.insertSales(cart)
                        cart = emptyList()
                        query = ""
                        searchedProducts = emptyList()
                        quantityToBuy = 1
                        navController.navigate("product_list") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar Venda")
                }
            }
        }
    }
}
