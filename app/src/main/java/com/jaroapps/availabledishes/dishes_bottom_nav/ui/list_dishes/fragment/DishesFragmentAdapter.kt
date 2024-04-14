package com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DishesFragmentAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllDishesFragment.newInstance()
            else -> AvailableDishesFragment.newInstance()
        }
    }
}
