package com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentAvailableDishesBinding
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.fragment.DetailDishFragment
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.adapter.AllDishesAdapter
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model.AvailableDishesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailableDishesFragment : Fragment() {

    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private val viewModel: AvailableDishesViewModel by viewModels()
    private var _binding: FragmentAvailableDishesBinding? = null
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
        _binding = FragmentAvailableDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.availableDishesRv.adapter = adapter

        //Получить список доступных рецептов из дата слоя
        viewModel.getAvailableDishes()

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAvailableDishes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = AvailableDishesFragment()
    }
}