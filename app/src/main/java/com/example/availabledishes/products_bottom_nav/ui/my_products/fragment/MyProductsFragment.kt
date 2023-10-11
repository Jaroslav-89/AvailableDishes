package com.example.availabledishes.products_bottom_nav.ui.my_products.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentMyProductsBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import com.example.availabledishes.products_bottom_nav.ui.my_products.adapter.MyProductsAdapter
import com.example.availabledishes.products_bottom_nav.ui.my_products.view_model.MyProductsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyProductsFragment : Fragment() {

    private val viewModel: MyProductsViewModel by viewModel()
    private var _binding: FragmentMyProductsBinding? = null
    private val binding get() = _binding!!
    private val adapter = MyProductsAdapter(
        object : MyProductsAdapter.MyProductClickListener {
            override fun onProductClick(product: Product) {
                findNavController().navigate(
                    R.id.action_productsFragment_to_detailProduct,
                    DetailProductFragment.createArgs(product.name))
            }

            override fun onFavoriteToggleClick(product: Product) {
                viewModel.toggleFavorite(product)
            }

            override fun onBuyToggleClick(product: Product) {
                if (product.needToBuy != true){
                    viewModel.toggleBuy(product)
                }
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myProductsRv.adapter = adapter

        viewModel.getMyProductsList()

        binding.addProducts.setOnClickListener {
            findNavController().navigate(
                R.id.action_productsFragment_to_addProductsFragment
            )
        }

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(productsList: List<Product>) {
        adapter.setProductsList(emptyList())
        adapter.setProductsList(productsList)
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyProductsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MyProductsFragment()
    }
}