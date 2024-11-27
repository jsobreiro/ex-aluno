package com.jhonathan.vendasnanuvem.ui.theme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    // Itens do menu inferior
    val items = listOf(
        BottomNavItem("Produtos", "product_list", Icons.Filled.Home),
        BottomNavItem("Vendas", "sales_screen", Icons.Filled.ShoppingCart),
        BottomNavItem("Relatório", "sales_report", Icons.Filled.PieChart),
        BottomNavItem("Pesquisar", "product_search", Icons.Filled.Search) // Novo menu
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

// Classe para representar itens do menu
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
