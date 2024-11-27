package com.jhonathan.vendasnanuvem.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jhonathan.vendasnanuvem.data.dao.ProductDao
import com.jhonathan.vendasnanuvem.data.dao.SaleDao
import com.jhonathan.vendasnanuvem.data.model.Product
import com.jhonathan.vendasnanuvem.data.model.Sale

@Database(entities = [Product::class, Sale::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
}

