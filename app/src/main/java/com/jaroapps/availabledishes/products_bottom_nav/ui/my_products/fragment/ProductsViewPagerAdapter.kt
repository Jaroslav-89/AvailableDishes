package com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProductsViewPagerAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyProductsFragment.newInstance()
            else -> BuyProductsFragment.newInstance()
        }
    }
}