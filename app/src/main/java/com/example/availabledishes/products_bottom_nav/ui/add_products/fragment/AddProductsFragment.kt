package com.example.availabledishes.products_bottom_nav.ui.add_products.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentAddProductsBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.add_products.adapter.ProductsAdapter
import com.example.availabledishes.products_bottom_nav.ui.add_products.view_model.AddProductsViewModel
import com.example.availabledishes.products_bottom_nav.ui.create_product.fragment.CreateProductFragment
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
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
    ): View? {
        binding = FragmentAddProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllProductsList()

        binding.rvProducts.adapter = adapter

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

        binding.createProducts.setOnClickListener {
            findNavController().navigate(
                R.id.action_addProductsFragment_to_createProduct,
                CreateProductFragment.createArgs(null)
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

    override fun onResume() {
        super.onResume()
        viewModel.getAllProductsList()
    }

    private fun renderState(productsList: List<Product>) {
        adapter.setProductsList(emptyList())
        adapter.setProductsList(productsList.filter { product ->
            product.name.lowercase(Locale.ROOT).contains(queryText.lowercase())
        })
        adapter.notifyDataSetChanged()
    }

    companion object {


//        const val ARGS_FACT = "fact"
//
//        fun createArgs(fact: String): Bundle =
//            bundleOf(ARGS_FACT to fact)
//           fun newInstance() = AddProductsFragment()
    }
}