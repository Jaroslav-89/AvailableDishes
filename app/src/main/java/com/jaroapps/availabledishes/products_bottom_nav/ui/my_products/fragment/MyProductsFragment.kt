package com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentMyProductsBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.ui.add_products.fragment.AddProductsFragment
import com.jaroapps.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.adapter.MyProductsAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.view_model.MyProductsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyProductsFragment : Fragment(R.layout.fragment_my_products) {

    private val viewModel: MyProductsViewModel by viewModel()
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
        if(itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun getProductList() {
        viewModel.getMyProductsList()
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