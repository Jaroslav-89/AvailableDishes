package com.jaroapps.availabledishes.products_bottom_nav.data

import com.jaroapps.availabledishes.common.data.convertors.ProductDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.TagDbConvertor
import com.jaroapps.availabledishes.common.data.db.AppDataBase
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ProductsRepositoryImpl(
    private val dataBase: AppDataBase,
    private val productDbConvertor: ProductDbConvertor,
    private val dishDbConvertor: com.jaroapps.availabledishes.common.data.convertors.DishDbConvertor,
    private val tagDbConvertor: TagDbConvertor,
) : ProductsRepository {
    override suspend fun createNewProduct(product: Product) {
        dataBase.productDao().upsertProduct(productDbConvertor.map(product))
    }

    override suspend fun changeProduct(product: Product, newProduct: Product) {
        if (product.name != newProduct.name) {
            dataBase.productDao()
                .changeProductWithName(product.name, productDbConvertor.map(newProduct))
        } else {
            dataBase.productDao().upsertProduct(productDbConvertor.map(newProduct))
        }
    }

    override suspend fun toggleFavorite(product: Product) {
        val productFromDb = dataBase.productDao().getProductByName(product.name)
        val productAfterChange = if (productFromDb.inFavorite) {
            productFromDb.copy(inFavorite = false, needToBuy = false)
        } else {
            productFromDb.copy(inFavorite = true)
        }
        dataBase.productDao().upsertProduct(productAfterChange)
    }

    override suspend fun toggleBuy(product: Product) {
        val productFromDb = dataBase.productDao().getProductByName(product.name)
        val productAfterChange = if (productFromDb.inFavorite) {
            productFromDb.copy(needToBuy = !productFromDb.needToBuy)
        } else {
            productFromDb.copy(inFavorite = true, needToBuy = true)
        }
        dataBase.productDao().upsertProduct(productAfterChange)
    }

    override suspend fun toggleDishFavorite(dish: Dish) {
        val dishFromDb = dataBase.dishDao().getDishByName(dish.name)
        val dishAfterChange = if (dishFromDb.inFavorite) {
            dishFromDb.copy(inFavorite = false)
        } else {
            dishFromDb.copy(inFavorite = true)
        }
        dataBase.dishDao().upsertDish(dishAfterChange)
    }

    override suspend fun getAllProductsList(): List<Product> {
        return productDbConvertor.mapList(dataBase.productDao().getAllProducts())
    }

    override suspend fun deleteProduct(product: Product) {
        dataBase.productDao().deleteProduct(product.name)
    }

    override fun getMyProductsList(): Flow<List<Product>> {
        return dataBase.productDao().getMyProducts().map(productDbConvertor::mapList)
    }

    override suspend fun getBuyProductsList(): List<Product> {
        return productDbConvertor.mapList(dataBase.productDao().getBuyProducts())
    }

    override suspend fun getProductByName(productName: String): Product {
        return productDbConvertor.map(dataBase.productDao().getProductByName(productName))
    }

    override fun getProductTagList(tags: List<String>): Flow<List<Tag>> = flow {
        val tagList = mutableListOf<Tag>()
        tags.forEach { tagStr ->
            tagList.add(tagDbConvertor.map(dataBase.tagDao().getProductTagByName(tagStr)))
        }
        emit(tagList)
    }

    override fun getAllProductTags(): Flow<List<Tag>> {
        return dataBase.tagDao().getAllProductTag().map(tagDbConvertor::mapList)
    }

    override suspend fun getAllDishesWithThisProduct(product: Product): List<Dish> {
        val dishesList = dishDbConvertor.mapList(dataBase.dishDao().getAllDish())
        val filteredDishesList = dishesList.filter { dish ->
            dish.ingredients.contains(product.name)
        }
        return filteredDishesList
    }

    override suspend fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean {
        val productList = dataBase.productDao().getAllProducts()
        val filteredProductList = productList.filter { it ->
            newNameForCheck.lowercase().trim() == it.name.lowercase().trim()
        }
        return filteredProductList.isEmpty()
    }
}