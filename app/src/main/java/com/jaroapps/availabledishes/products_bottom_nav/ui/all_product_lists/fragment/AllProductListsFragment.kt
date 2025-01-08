package com.jaroapps.availabledishes.products_bottom_nav.ui.all_product_lists.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentAllProductListsBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList
import com.jaroapps.availabledishes.products_bottom_nav.ui.all_product_lists.adapter.AllProductListsAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.all_product_lists.view_model.AllProductListsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllProductListsFragment : Fragment(R.layout.fragment_all_product_lists) {

    private val viewModel: AllProductListsViewModel by viewModels()
    private var _binding: FragmentAllProductListsBinding? = null
    private val binding get() = _binding!!

    private val adapter = AllProductListsAdapter(
        object : AllProductListsAdapter.AllProductListsClickListener {
            override fun onProductsListClick(productLists: ProductList) {
                val direction =
                    AllProductListsFragmentDirections
                        .actionAllProductListsFragmentToProductsFragment(productLists.id)
                findNavController().navigate(direction)
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllProductListsBinding.bind(view)

        setAdapter()
        getAllProductLists()
        setClickListeners()
        setObserver()
    }

    private fun setAdapter() {
        binding.allProductListsRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.allProductListsRv.adapter = adapter
        val itemAnimator = binding.allProductListsRv.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun getAllProductLists() {
        viewModel.getAllProductLists()
    }

    private fun setClickListeners() {
        binding.addNewProductsListBtn.setOnClickListener {
            val direction =
                AllProductListsFragmentDirections
                    .actionAllProductListsFragmentToEditCreateProductListFragment(
                        ""
                    )
            findNavController().navigate(direction)
        }
    }

    private fun setObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(allProductLists: List<ProductList>) {
        adapter.setAllProductLists(allProductLists)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = AllProductListsFragment()
    }
}