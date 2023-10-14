package com.example.availabledishes.products_bottom_nav.ui.add_products.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentAddProductsBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.add_products.adapter.ProductsAdapter
import com.example.availabledishes.products_bottom_nav.ui.add_products.view_model.AddProductsViewModel
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import com.example.availabledishes.products_bottom_nav.ui.edit_create_product.fragment.EditCreateProductFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class AddProductsFragment : Fragment() {

    private val viewModel: AddProductsViewModel by viewModel()
    private var queryText = ""

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

        setSearchQueryChangeListener()

        if (!requireArguments().getString(AddProductsFragment.SEARCH_REQUEST).isNullOrEmpty()) {
            binding.productSearch.setQuery(requireArguments().getString(SEARCH_REQUEST), true)
        }

        viewModel.getAllProductsList()

        binding.rvProducts.adapter = adapter

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

    private fun setSearchQueryChangeListener() {
        binding.productSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                queryText = if (newText != null) {
                    newText
                } else {
                    ""
                }
                viewModel.getAllProductsList()
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProductsList()
    }

    private fun renderState(productsList: List<Product>) {
        adapter.setProductsList(emptyList())
        val resultProductList = productsList.filter { product ->
            product.name.lowercase(Locale.ROOT).contains(queryText.lowercase())
        }.toMutableList()
        for (product in productsList) {
            if (product.tag != null) {
                for (tag in product.tag!!) {
                    if (tag.name.lowercase(Locale.ROOT)
                            .contains(queryText.lowercase()) && queryText.isNotEmpty()
                    ) {
                        resultProductList.add(product)
                    }
                }
            }
        }
        adapter.setProductsList(resultProductList)
        adapter.notifyDataSetChanged()
    }

    companion object {
        private const val SEARCH_REQUEST = "search_request"

        fun createArgs(searchRequest: String?): Bundle =
            bundleOf(SEARCH_REQUEST to searchRequest)
    }
}