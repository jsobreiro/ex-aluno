package com.jhonathan.vendasnanuvem

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.jhonathan.vendasnanuvem.ui.theme.VendasNaNuvemTheme
import com.jhonathan.vendasnanuvem.ui.theme.screens.SalesScreen
import com.jhonathan.vendasnanuvem.viewmodel.ProductViewModel

class SalesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializando os ViewModels
        val productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        val saleViewModel = ViewModelProvider(this)[SaleViewModel::class.java]

        setContent {
            VendasNaNuvemTheme {
                val navController = rememberNavController()

                // Tela de vendas com navegação
                SalesScreenContent(
                    navController = navController,
                    productViewModel = productViewModel,
                    saleViewModel = saleViewModel,
                    onNavigateBack = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Fecha SalesActivity ao voltar
                    }
                )
            }
        }
    }
}

@Composable
fun SalesScreenContent(
    navController: androidx.navigation.NavController,
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            // Botão para voltar à MainActivity
            androidx.compose.material3.Button(onClick = onNavigateBack) {
                androidx.compose.material3.Text("Voltar para Produtos")
            }
        }
    ) {
        SalesScreen(
            productViewModel = productViewModel,
            saleViewModel = saleViewModel,
            navController = navController
        )
    }
}
