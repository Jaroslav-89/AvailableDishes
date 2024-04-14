package com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentDishesBinding

class DishesFragment : Fragment() {

    private lateinit var binding: FragmentDishesBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPagerDishes.adapter = DishesFragmentAdapter(this)
        tabMediator = TabLayoutMediator(
            binding.tabLayoutDishes,
            binding.viewPagerDishes
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.all_dishes)
                1 -> tab.text = getString(R.string.available_dishes)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}