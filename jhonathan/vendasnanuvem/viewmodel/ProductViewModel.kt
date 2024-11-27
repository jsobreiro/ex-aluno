package com.jhonathan.vendasnanuvem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonathan.vendasnanuvem.data.model.Product
import com.jhonathan.vendasnanuvem.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val allProducts: Flow<List<Product>> = repository.allProducts

    // Inserir novo produto
    fun insert(product: Product) {
        viewModelScope.launch {
            repository.insert(product)
        }
    }

    // Deletar produto
    fun delete(product: Product) {
        viewModelScope.launch {
            repository.delete(product)
        }
    }

    // Atualizar produto existente
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    suspend fun getProductById(productId: Int): Product? {
        return repository.getProductById(productId)
    }

}



