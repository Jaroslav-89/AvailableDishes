package com.jaroapps.availabledishes.products_bottom_nav.ui.add_products.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentAddProductsBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.ui.add_products.adapter.ProductsAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.add_products.view_model.AddProductsViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product.fragment.EditCreateProductFragment
import com.jaroapps.availabledishes.products_bottom_nav.ui.product_detail.fragment.DetailProductFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AddProductsFragment : Fragment() {

    private val viewModel: AddProductsViewModel by viewModels()
    private var queryText = ""
    private val args: AddProductsFragmentArgs by navArgs()
    private lateinit var binding: FragmentAddProductsBinding
    private val adapter = ProductsAdapter(
        object : ProductsAdapter.ProductClickListener {
            override fun onProductClick(product: Product) {
                findNavController().navigate(
                    R.id.action_addProductsFragment_to_detailProduct,
                    DetailProductFragment.createArgs(product.name)
                )
            }

            override fun onFavoriteToggleClick(product: Product) {
                viewModel.toggleFavorite(product)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        viewModel.getAllProductsList()

        setSearchQueryChangeListener()

        if (args.searchRequest.isNotBlank()) {
            binding.productSearch.setQuery(requireArguments().getString(args.searchRequest), true)
        }

        binding.createProducts.setOnClickListener {
            findNavController().navigate(
                R.id.action_addProductsFragment_to_createProduct,
                EditCreateProductFragment.createArgs(null)
            )
        }

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.state.observe(viewLifecycleOwner)
        {
            renderState(it)
        }
    }

    private fun setAdapter() {
        binding.rvProducts.adapter = adapter
        val itemAnimator = binding.rvProducts.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun setSearchQueryChangeListener() {
        binding.productSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                queryText = newText ?: ""
                viewModel.getAllProductsList()
                return true
            }
        })
    }

    private fun renderState(productsList: List<Product>) {
        val resultProductList = productsList.filter { product ->
            product.name.lowercase(Locale.ROOT).contains(queryText.lowercase())
        }.toMutableList()
        for (product in productsList) {
            for (tag in product.tag) {
                if (tag.lowercase()
                        .contains(queryText.lowercase()) && queryText.isNotEmpty()
                ) {
                    resultProductList.add(product)
                }
            }
        }
        adapter.setProductsList(resultProductList)
    }
}