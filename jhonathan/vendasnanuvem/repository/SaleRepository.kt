import com.jhonathan.vendasnanuvem.data.dao.ProductDao
import com.jhonathan.vendasnanuvem.data.dao.SaleDao
import com.jhonathan.vendasnanuvem.data.model.Sale

class SaleRepository(
    private val saleDao: SaleDao,
    private val productDao: ProductDao
) {
    // Insere uma única venda
    suspend fun insertSale(sale: Sale) {
        saleDao.insertSale(sale)
        updateProductStock(sale.productId, sale.quantity)
    }

    // Insere múltiplas vendas
    suspend fun insertSales(sales: List<Sale>) {
        for (sale in sales) {
            saleDao.insertSale(sale)
            updateProductStock(sale.productId, sale.quantity)
        }
    }

    // Atualiza o estoque do produto
    private suspend fun updateProductStock(productId: Int, quantitySold: Int) {
        val product = productDao.getProductById(productId)
        product?.let {
            val updatedProduct = it.copy(quantity = it.quantity - quantitySold)
            productDao.updateProduct(updatedProduct)
        }
    }

    // Busca todas as vendas
    fun getAllSales() = saleDao.getAllSales()
}
