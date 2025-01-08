package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.all_product_lists.fragment.AllProductListsFragment

class ProductsViewPagerAdapter(parentFragment: Fragment, private val productListId: String) : FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductListDetailFragment.newInstance(productListId)
            else -> BuyProductsFragment.newInstance(productListId)
        }
    }
}