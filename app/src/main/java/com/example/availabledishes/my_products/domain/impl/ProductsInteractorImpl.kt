package com.example.availabledishes.my_products.domain.impl

import com.example.availabledishes.my_products.domain.api.ProductsInteractor
import com.example.availabledishes.my_products.domain.api.ProductsRepository
import java.util.concurrent.Executors

class ProductsInteractorImpl(private val repository: ProductsRepository) : ProductsInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun getProductsList(
        expression: String,
        consumer: ProductsInteractor.ProductsConsumer
    ) {
        executor.execute {
            consumer.consume(repository.getProductsList(expression))
        }
    }
}