package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentProductListDetailBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.adapter.MyProductsAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model.ProductListDetailState
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model.ProductListDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListDetailFragment(private val productListId: String) :
    Fragment(R.layout.fragment_product_list_detail) {

    private val viewModel: ProductListDetailViewModel by viewModels()
    private var _binding: FragmentProductListDetailBinding? = null
    private val binding get() = _binding!!

    private val adapter = MyProductsAdapter(
        object : MyProductsAdapter.MyProductClickListener {
            override fun onProductClick(product: Product) {
                val direction =
                    ProductsFragmentDirections.actionProductsFragmentToDetailProduct(product.name, productListId)
                findNavController().navigate(direction)
            }

            override fun onFavoriteToggleClick(product: Product) {
                viewModel.toggleFavorite(product)
            }

            override fun onBuyToggleClick(product: Product) {
//                if (!product.needToBuy) {
//                    viewModel.toggleBuy(product)
//                }
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductListDetailBinding.bind(view)

        setAdapter()
        getProductList()
        setClickListeners()
        setObserver()
    }

    private fun setAdapter() {
        binding.myProductsRv.adapter = adapter
        val itemAnimator = binding.myProductsRv.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun getProductList() {
        viewModel.getProductsInList(
            listId = productListId
        )
    }

    private fun setClickListeners() {
        binding.addProducts.setOnClickListener {
            val direction = ProductsFragmentDirections.actionProductsFragmentToAddProductsFragment(
                productListId, ""
            )
            findNavController().navigate(direction)
        }
    }

    private fun setObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: ProductListDetailState) {
        when (state) {
            is ProductListDetailState.Loading -> {

            }

            is ProductListDetailState.Content -> {
                adapter.setProductsList(state.productsList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(productListId: String) = ProductListDetailFragment(productListId)
    }
}