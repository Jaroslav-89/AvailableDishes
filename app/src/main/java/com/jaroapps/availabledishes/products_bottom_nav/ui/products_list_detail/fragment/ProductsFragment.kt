package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentProductsBinding
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model.ProductFragmentState
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model.ProductFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel: ProductFragmentViewModel by viewModels()
    private lateinit var binding: FragmentProductsBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val args: ProductsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProductListInfo()
        setViewPager()
        setClickListeners()
        setObserver()
    }

    private fun getProductListInfo() {
        viewModel.getProductListInfo(args.productListId)
    }

    private fun setViewPager() {
        binding.viewPagerProducts.adapter = ProductsViewPagerAdapter(this, args.productListId)
        tabMediator = TabLayoutMediator(
            binding.tabLayoutProducts,
            binding.viewPagerProducts
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.my_products)
                1 -> tab.text = getString(R.string.buy_products)
            }
        }
        tabMediator.attach()
    }

    private fun setClickListeners() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: ProductFragmentState) {
        when (state) {
            is ProductFragmentState.Loading -> {

            }

            is ProductFragmentState.Content -> {
                binding.headingProductList.text = state.productListInfo.name
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}