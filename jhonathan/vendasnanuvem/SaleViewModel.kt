package com.jhonathan.vendasnanuvem

import SaleRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonathan.vendasnanuvem.data.model.Sale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SaleViewModel(private val repository: SaleRepository) : ViewModel() {

    // Insere uma única venda
    fun insertSale(productId: Int, quantity: Int, totalValue: Double) {
        viewModelScope.launch {
            val sale = Sale(
                productId = productId,
                quantity = quantity,
                totalValue = totalValue
            )
            repository.insertSale(sale)
        }
    }

    // Insere múltiplas vendas
    fun insertSales(sales: List<Sale>) {
        viewModelScope.launch {
            repository.insertSales(sales) // Chamando o método do SaleRepository
        }
    }

    // Busca todas as vendas
    fun getAllSales(): Flow<List<Sale>> = repository.getAllSales()
}
