package com.example.availabledishes.dishes_bottom_nav.ui.list_dishes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentAllDishesBinding
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.fragment.DetailDishFragment
import com.example.availabledishes.dishes_bottom_nav.ui.list_dishes.adapter.AllDishesAdapter
import com.example.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model.AllDishesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllDishes : Fragment() {

    private var backPressedTime: Long = 0
    private var queryText = ""
    private lateinit var backToast: Toast
    private val viewModel: AllDishesViewModel by viewModel()
    private var _binding: FragmentAllDishesBinding? = null
    private val binding get() = _binding!!
    private val adapter = AllDishesAdapter(
        object : AllDishesAdapter.DishClickListener {
            override fun onDishClick(dish: Dish) {
                findNavController().navigate(
                    R.id.action_dishesFragment_to_detailDishFragment,
                    DetailDishFragment.createArgs(dish.name)
                )
            }

            override fun onFavoriteToggleClick(dish: Dish) {
                viewModel.toggleFavorite(dish)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allDishesRv.adapter = adapter

        viewModel.getAllDishes()

        if (arguments != null) {
            if (requireArguments().getString(SEARCH_REQUEST_DISH_TAG) != null) {
                binding.dishSearch.setQuery(
                    requireArguments().getString(SEARCH_REQUEST_DISH_TAG),
                    true
                )
            }
        }


        binding.addNewDish.setOnClickListener {
            //  findNavController().navigate(
            // R.id.action_productsFragment_to_addProductsFragment,
            //   AddProductsFragment.createArgs(null)
            // )
        }

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        backToast.cancel()
                        requireActivity().finish()
                        return
                    } else {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.double_back),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    backPressedTime = System.currentTimeMillis()
                }
            })
    }

    private fun renderState(dishesList: List<Dish>) {
        adapter.setDishesList(emptyList())
        adapter.setDishesList(dishesList)
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllDishes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SEARCH_REQUEST_DISH_TAG = "search_request_dish_tag"
        fun newInstance() = AllDishes()

        fun createArgs(searchRequest: String?): Bundle =
            bundleOf(SEARCH_REQUEST_DISH_TAG to searchRequest)
    }
}
