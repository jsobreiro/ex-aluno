package com.jhonathan.vendasnanuvem.data.dao

import androidx.room.*
import com.jhonathan.vendasnanuvem.data.model.Sale
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {
    // Insere uma venda no banco de dados
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale)

    // Retorna todas as vendas
    @Query("SELECT * FROM sales")
    fun getAllSales(): Flow<List<Sale>>
}
