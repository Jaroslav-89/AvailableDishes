package com.example.availabledishes.products_bottom_nav.ui.my_products.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentProductsBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProductsFragment : Fragment(){

    private lateinit var binding: FragmentProductsBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPagerProducts.adapter = ProductsViewPagerAdapter(this)
        tabMediator = TabLayoutMediator(binding.tabLayoutProducts, binding.viewPagerProducts) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.my_products)
                1 -> tab.text = getString(R.string.buy_products)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}