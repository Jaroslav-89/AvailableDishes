package com.jaroapps.availabledishes.notes_bottom_nav.ui.my_refrigerators.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentMyRefrigeratorsBinding
import com.jaroapps.availabledishes.notes_bottom_nav.domain.model.Refrigerator
import com.jaroapps.availabledishes.notes_bottom_nav.ui.my_refrigerators.adapter.MyRefrigeratorsAdapter
import com.jaroapps.availabledishes.notes_bottom_nav.ui.my_refrigerators.view_model.MyRefrigeratorsState
import com.jaroapps.availabledishes.notes_bottom_nav.ui.my_refrigerators.view_model.MyRefrigeratorsViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.product_detail.fragment.DetailProductFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRefrigeratorsFragment : Fragment(R.layout.fragment_my_refrigerators) {

    private val viewModel: MyRefrigeratorsViewModel by viewModels()
    private var _binding: FragmentMyRefrigeratorsBinding? = null
    private val binding get() = _binding!!

    private val adapter = MyRefrigeratorsAdapter(
        object : MyRefrigeratorsAdapter.MyRefrigeratorsClickListener {
            override fun onRefrigeratorClick(refrigerator: Refrigerator) {
                findNavController().navigate(
                    R.id.action_productsFragment_to_detailProduct,
                    DetailProductFragment.createArgs(refrigerator.name)
                )
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyRefrigeratorsBinding.bind(view)

        setAdapter()
        getProductList()
        setClickListeners()
        setObserver()
    }

    private fun setAdapter() {

    }

    private fun getProductList() {

    }

    private fun setClickListeners() {
        binding.addNewRefrigeratorBtn.setOnClickListener {
            val direction =
                MyRefrigeratorsFragmentDirections.actionMyRefrigeratorsFragmentToEditCreateRefrigeratorFragment(
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


    private fun renderState(state: MyRefrigeratorsState) {
        when (state) {
            is MyRefrigeratorsState.Loading -> {
                showLoading()
            }

            is MyRefrigeratorsState.Content -> {
                showContent(state.refrigerators)
            }
        }
    }

    private fun showLoading() {

    }

    private fun showContent(refrigeratorsList: List<Refrigerator>) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}