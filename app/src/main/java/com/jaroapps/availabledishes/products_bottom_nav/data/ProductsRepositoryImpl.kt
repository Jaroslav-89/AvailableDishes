package com.jaroapps.availabledishes.products_bottom_nav.data

import com.jaroapps.availabledishes.common.data.convertors.DishDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.ProductDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.TagDbConvertor
import com.jaroapps.availabledishes.common.data.db.AppDataBase
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.data.convertors.ProductListDbConvertor
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ProductsRepositoryImpl(
    private val dataBase: AppDataBase,
) : ProductsRepository {
    //Products
    override suspend fun createNewProduct(product: Product) {
        dataBase.productDao().upsertProduct(ProductDbConvertor.map(product))
    }

    override suspend fun changeProduct(product: Product, newProduct: Product) {
        if (product.name != newProduct.name) {
            dataBase.productDao()
                .changeProductWithName(product.name, ProductDbConvertor.map(newProduct))
        } else {
            dataBase.productDao().upsertProduct(ProductDbConvertor.map(newProduct))
        }
    }

    override suspend fun getAllProducts(): List<Product> {
        return ProductDbConvertor.mapList(dataBase.productDao().getAllProducts())
    }

    override suspend fun toggleAddProductToList(productName: String, productListId: String) {
        val productList =
            ProductListDbConvertor.map(dataBase.productListDao().getProductListById(productListId))

        if (productList.productsInThisList.contains(productName)) {
            val updateProductsInThisList = productList.productsInThisList.toMutableList()
            updateProductsInThisList.remove(productName)
            val updateProductList = productList.copy(
                productsInThisList = updateProductsInThisList,
                numberOfProducts = updateProductsInThisList.size
            )
            dataBase.productListDao()
                .upsertProductList(ProductListDbConvertor.map(updateProductList))
        } else {
            val updateProductsInThisList = productList.productsInThisList.toMutableList()
            updateProductsInThisList.add(productName)
            val updateProductList = productList.copy(
                productsInThisList = updateProductsInThisList,
                numberOfProducts = updateProductsInThisList.size
            )
            dataBase.productListDao()
                .upsertProductList(ProductListDbConvertor.map(updateProductList))
        }

        val product = ProductDbConvertor.map(dataBase.productDao().getProductByName(productName))
        if (product.productLists.contains(productListId)) {
            val productLists = product.productLists.toMutableList()
            productLists.remove(productListId)
            dataBase.productDao()
                .upsertProduct(ProductDbConvertor.map(product.copy(productLists = productLists)))
        } else {
            val productLists = product.productLists.toMutableList()
            productLists.add(productListId)
            dataBase.productDao()
                .upsertProduct(ProductDbConvertor.map(product.copy(productLists = productLists)))
        }
    }

    override suspend fun deleteProduct(product: Product) {
        dataBase.productDao().deleteProduct(product.name)
    }

    override suspend fun getProductByName(productName: String): Product {
        return ProductDbConvertor.map(dataBase.productDao().getProductByName(productName))
    }


    override suspend fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean {
        val productList = dataBase.productDao().getAllProducts()
        val filteredProductList = productList.filter { it ->
            newNameForCheck.lowercase().trim() == it.name.lowercase().trim()
        }
        return filteredProductList.isEmpty()
    }

    override suspend fun getAllDishesWithThisProduct(product: Product): List<Dish> {
        val dishesList = DishDbConvertor.mapList(dataBase.dishDao().getAllDish())
        val filteredDishesList = dishesList.filter { dish ->
            dish.ingredients.contains(product.name)
        }
        return filteredDishesList
    }

    override fun getAllProductTags(): Flow<List<Tag>> {
        return dataBase.tagDao().getAllProductTag().map(TagDbConvertor::mapList)
    }

    override fun getProductTagList(tags: List<String>): Flow<List<Tag>> = flow {
        val tagList = mutableListOf<Tag>()
        tags.forEach { tagStr ->
            tagList.add(TagDbConvertor.map(dataBase.tagDao().getProductTagByName(tagStr)))
        }
        emit(tagList)
    }

    //ProductLists
    override fun getAllProductLists(): Flow<List<ProductList>> {
        return dataBase.productListDao().getAllProductLists().map(ProductListDbConvertor::mapList)
    }

    override suspend fun editCreateProductList(productList: ProductList) {
        dataBase.productListDao().upsertProductList(ProductListDbConvertor.map((productList)))
    }

    override suspend fun getProductListById(productListId: String): ProductList {
        return ProductListDbConvertor.map(
            dataBase.productListDao().getProductListById(productListId)
        )
    }

    override suspend fun deleteProductList(productListId: String) {
        dataBase.productListDao().deleteProductList(productListId)
    }

    override suspend fun getProductsInList(listId: String): List<Product> {
        val productList =
            ProductListDbConvertor.map(dataBase.productListDao().getProductListById(listId))
        val productsInThisList = mutableListOf<Product>()
        for (productStr in productList.productsInThisList) {
            val product = ProductDbConvertor.map(dataBase.productDao().getProductByName(productStr))
            productsInThisList.add(product)
        }
        return productsInThisList
    }

    override suspend fun getBuyProductsList(): List<Product> {
        return ProductDbConvertor.mapList(dataBase.productDao().getAllProducts())
    }

    //Dishes
    override suspend fun toggleDishFavorite(dish: Dish) {
        val dishFromDb = dataBase.dishDao().getDishByName(dish.name)
        val dishAfterChange = if (dishFromDb.inFavorite) {
            dishFromDb.copy(inFavorite = false)
        } else {
            dishFromDb.copy(inFavorite = true)
        }
        dataBase.dishDao().upsertDish(dishAfterChange)
    }


    //
    override suspend fun toggleFavorite(product: Product) {
//        val productFromDb = dataBase.productDao().getProductByName(product.name)
//        val productAfterChange = if (productFromDb.inFavorite) {
//            productFromDb.copy(inFavorite = false, needToBuy = false)
//        } else {
//            productFromDb.copy(inFavorite = true)
//        }
//        dataBase.productDao().upsertProduct(productAfterChange)
    }

    override suspend fun toggleBuy(product: Product) {
//        val productFromDb = dataBase.productDao().getProductByName(product.name)
//        val productAfterChange = if (productFromDb.inFavorite) {
//            productFromDb.copy(needToBuy = !productFromDb.needToBuy)
//        } else {
//            productFromDb.copy(inFavorite = true, needToBuy = true)
//        }
//        dataBase.productDao().upsertProduct(productAfterChange)
    }
}