package com.jhonathan.vendasnanuvem.ui.theme.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jhonathan.vendasnanuvem.ui.theme.components.BottomNavigationBar
import com.jhonathan.vendasnanuvem.viewmodel.ProductViewModel
import com.jhonathan.vendasnanuvem.SaleViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "product_list",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tela de lista de produtos
            composable("product_list") {
                ProductListScreen(
                    viewModel = productViewModel,
                    onAddProduct = { navController.navigate("product_form/0") },
                    onEditProduct = { productId ->
                        navController.navigate("product_form/$productId")
                    }
                )
            }
            // Tela de formulário de produto
            composable("product_form/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                ProductFormScreen(
                    viewModel = productViewModel,
                    productId = productId,
                    onBack = { navController.popBackStack() }
                )
            }
            // Tela de vendas
            composable("sales_screen") {
                SalesScreen(
                    productViewModel = productViewModel,
                    saleViewModel = saleViewModel,
                    navController = navController // Passando o NavController diretamente
                )
            }

            // Tela de relatório de vendas
            composable("sales_report") {
                SalesReportScreen(
                    productViewModel = productViewModel,
                    saleViewModel = saleViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            // Tela de pesquisa de produtos
            composable("product_search") {
                ProductSearchScreen(
                    productViewModel = productViewModel,
                    onEditProduct = { productId ->
                        navController.navigate("product_form/$productId")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

        }
    }
}
