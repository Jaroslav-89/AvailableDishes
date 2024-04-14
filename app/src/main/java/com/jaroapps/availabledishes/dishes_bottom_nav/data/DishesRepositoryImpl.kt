package com.jaroapps.availabledishes.dishes_bottom_nav.data

import com.jaroapps.availabledishes.common.data.convertors.DishDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.ProductDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.TagDbConvertor
import com.jaroapps.availabledishes.common.data.db.AppDataBase
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DishesRepositoryImpl(
    private val dataBase: AppDataBase,
) : DishesRepository {
    override suspend fun createNewDish(dish: Dish) {
        dataBase.dishDao().upsertDish(DishDbConvertor.map(dish))
    }

    override suspend fun changeDish(dishForChange: Dish, newDish: Dish) {
        if (dishForChange.name != newDish.name) {
            dataBase.dishDao().changeDishWithName(dishForChange.name, DishDbConvertor.map(newDish))
        } else {
            dataBase.dishDao().upsertDish(DishDbConvertor.map(newDish))
        }
    }

    override suspend fun toggleFavorite(dish: Dish) {
        val dishFromDb = dataBase.dishDao().getDishByName(dish.name)
        val dishAfterChange = if (dishFromDb.inFavorite) {
            dishFromDb.copy(inFavorite = false)
        } else {
            dishFromDb.copy(inFavorite = true)
        }
        dataBase.dishDao().upsertDish(dishAfterChange)
    }

    override suspend fun toggleFavoriteProduct(product: Product) {
        val productFromDb = dataBase.productDao().getProductByName(product.name)
        val productAfterChange = if (productFromDb.inFavorite) {
            productFromDb.copy(inFavorite = false, needToBuy = false)
        } else {
            productFromDb.copy(inFavorite = true)
        }
        dataBase.productDao().upsertProduct(productAfterChange)
    }

    override suspend fun toggleBuyProduct(product: Product) {
        val productFromDb = dataBase.productDao().getProductByName(product.name)
        val productAfterChange = productFromDb.copy(needToBuy = product.needToBuy)
        dataBase.productDao().upsertProduct(productAfterChange)
    }

    override suspend fun deleteDish(dish: Dish) {
        dataBase.dishDao().deleteDish(dish.name)
    }

    override suspend fun getAllDishes(): List<Dish> {
        return DishDbConvertor.mapList(dataBase.dishDao().getAllDish())
    }

    override suspend fun getDishByName(dishName: String): Dish {
        return DishDbConvertor.map(dataBase.dishDao().getDishByName(dishName))
    }

    override suspend fun getDishTagList(tags: List<String>): Flow<List<Tag>> = flow {
        val tagList = mutableListOf<Tag>()
        tags.forEach { tagStr ->
            tagList.add(TagDbConvertor.map(dataBase.tagDao().getDishTagByName(tagStr)))
        }
        emit(tagList)
    }

    override suspend fun getDishIngredients(products: List<String>): List<Product> {
        val productList = mutableListOf<Product>()
        products.forEach { product ->
            productList.add(ProductDbConvertor.map(dataBase.productDao().getProductByName(product)))
        }
        return productList
    }

    override suspend fun getAllDishesTags(): List<Tag> {
        return TagDbConvertor.mapList(dataBase.tagDao().getAllDishTag())
    }

    override suspend fun getAvailableDishes(): List<Dish> {
        val allDishes = DishDbConvertor.mapList(dataBase.dishDao().getAllDish())
        val myProductsStrList = mutableListOf<String>()
        var availableDishes = emptyList<Dish>()

        dataBase.productDao().getMyProducts().collect() {
            val myProducts = ProductDbConvertor.mapList(it)
            myProducts.forEach { product -> myProductsStrList.add(product.name) }
            availableDishes = allDishes.filter { dish ->
                myProductsStrList.containsAll(dish.ingredients)
            }
        }
        return availableDishes
    }

    override suspend fun getQueryProducts(query: String): List<Product> {
        return ProductDbConvertor.mapList(dataBase.productDao().getQueryProducts(query))
    }

    override suspend fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean {
        val dishList = dataBase.dishDao().getAllDish()
        val filteredDishesList = dishList.filter { dishEntity ->
            newNameForCheck.lowercase().trim() == dishEntity.name.lowercase().trim()
        }
        return filteredDishesList.isEmpty()
    }
}