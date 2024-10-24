package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentMyProductsBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.ui.add_products.fragment.AddProductsFragment
import com.jaroapps.availabledishes.products_bottom_nav.ui.product_detail.fragment.DetailProductFragment
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.adapter.MyProductsAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model.MyProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProductsFragment : Fragment(R.layout.fragment_my_products) {

    private val viewModel: MyProductsViewModel by viewModels()
    private var _binding: FragmentMyProductsBinding? = null
    private val binding get() = _binding!!
    private val adapter = MyProductsAdapter(
        object : MyProductsAdapter.MyProductClickListener {
            override fun onProductClick(product: Product) {
                findNavController().navigate(
                    R.id.action_productsFragment_to_detailProduct,
                    DetailProductFragment.createArgs(product.name)
                )
            }

            override fun onFavoriteToggleClick(product: Product) {
                viewModel.toggleFavorite(product)
            }

            override fun onBuyToggleClick(product: Product) {
                if (!product.needToBuy) {
                    viewModel.toggleBuy(product)
                }
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyProductsBinding.bind(view)

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

    //TODO добавить safeargs, принимать id списка
    private fun getProductList() {
        viewModel.getProductsInList(listId = "")
    }

    private fun setClickListeners() {
        binding.addProducts.setOnClickListener {
            findNavController().navigate(
                R.id.action_productsFragment_to_addProductsFragment,
                AddProductsFragment.createArgs(null)
            )
        }
    }

    private fun setObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(productsList: List<Product>) {
        adapter.setProductsList(productsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MyProductsFragment()
    }
}