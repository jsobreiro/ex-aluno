package com.jhonathan.vendasnanuvem.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhonathan.vendasnanuvem.data.model.Product
import com.jhonathan.vendasnanuvem.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    viewModel: ProductViewModel,
    productId: Int?,
    onBack: () -> Unit
) {
    // Estado para armazenar o produto
    var product by remember { mutableStateOf<Product?>(null) }

    // Estados para os campos do formulário
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    // Função para formatar o valor como moeda
    fun formatCurrency(input: String): String {
        return try {
            val cleanInput = input.replace(Regex("[^\\d]"), "").toDoubleOrNull() ?: 0.0
            "R$ ${"%.2f".format(cleanInput / 100).replace('.', ',')}"
        } catch (e: Exception) {
            "R$ 0,00"
        }
    }

    // Carregar produto quando o `productId` é fornecido
    LaunchedEffect(productId) {
        if (productId != null && productId != 0) {
            product = viewModel.getProductById(productId) // Busca o produto
            product?.let {
                name = it.name
                description = it.description
                quantity = it.quantity.toString()
                // Formatar valor somente para exibição
                value = "R$ ${"%.2f".format(it.value).replace('.', ',')}"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId != null && productId != 0) "Editar Produto" else "Novo Produto") },
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
            // Campo: Nome
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome do Produto") },
                modifier = Modifier.fillMaxWidth()
            )
            // Campo: Descrição
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )
            // Campo: Quantidade
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth()
            )
            // Campo: Valor com máscara
            OutlinedTextField(
                value = value,
                onValueChange = {
                    value = formatCurrency(it) // Aplicar máscara em tempo real
                },
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth()
            )
            // Botão Salvar
            Button(
                onClick = {
                    // Remover máscara para salvar
                    val parsedValue = value.replace("R$", "").replace(",", ".").trim().toDoubleOrNull() ?: 0.0
                    val updatedProduct = Product(
                        id = productId ?: 0,
                        name = name,
                        description = description,
                        quantity = quantity.toIntOrNull() ?: 0,
                        value = parsedValue
                    )
                    if (productId != null && productId != 0) {
                        viewModel.updateProduct(updatedProduct)
                    } else {
                        viewModel.insert(updatedProduct)
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}

