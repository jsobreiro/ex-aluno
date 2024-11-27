package com.jhonathan.vendasnanuvem

import SaleRepository
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.jhonathan.vendasnanuvem.data.database.AppDatabase
import com.jhonathan.vendasnanuvem.repository.ProductRepository
import com.jhonathan.vendasnanuvem.ui.theme.VendasNaNuvemTheme
import com.jhonathan.vendasnanuvem.ui.theme.screens.AppNavHost
import com.jhonathan.vendasnanuvem.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o banco de dados
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "products_db"
        ).build()

        // Criação dos DAOs
        val productDao = database.productDao()
        val saleDao = database.saleDao()

        // Criação dos Repositórios
        val productRepository = ProductRepository(productDao)
        val saleRepository = SaleRepository(saleDao, productDao)

        // Inicialização dos ViewModels
        val productViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProductViewModel(productRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        })[ProductViewModel::class.java]

        val saleViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SaleViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SaleViewModel(saleRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        })[SaleViewModel::class.java]

        // Define o conteúdo da aplicação
        setContent {
            VendasNaNuvemTheme {
                val navController = rememberNavController()

                AppNavHost(
                    navController = navController,
                    productViewModel = productViewModel,
                    saleViewModel = saleViewModel
                )
            }
        }
    }
}

