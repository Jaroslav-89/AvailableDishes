package com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentAllDishesBinding
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.fragment.DetailDishFragment
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.edit_create_dish.fragment.EditeCreateDishFragment
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.adapter.AllDishesAdapter
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model.AllDishesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllDishesFragment : Fragment() {

    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private val viewModel: AllDishesViewModel by viewModels()
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
            findNavController().navigate(
                R.id.action_dishesFragment_to_editeCreateDishFragment,
                EditeCreateDishFragment.createArgs(null)
            )
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
        adapter.setDishesList(dishesList)
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
        fun newInstance() = AllDishesFragment()

        fun createArgs(searchRequest: String?): Bundle =
            bundleOf(SEARCH_REQUEST_DISH_TAG to searchRequest)
    }
}
