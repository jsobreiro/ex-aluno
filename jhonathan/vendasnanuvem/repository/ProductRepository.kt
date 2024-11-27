package com.jhonathan.vendasnanuvem.repository

import com.jhonathan.vendasnanuvem.data.dao.ProductDao
import com.jhonathan.vendasnanuvem.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insert(product: Product) = productDao.insertProduct(product)

    suspend fun delete(product: Product) = productDao.deleteProduct(product)

    suspend fun updateProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun getProductById(productId: Int): Product? {
        return productDao.getProductById(productId)
    }



}