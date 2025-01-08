package com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product_list.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentEditCreateProductListBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList
import com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product_list.view_model.EditCreateProductListViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.fragment.ProductsFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCreateProductListFragment : Fragment(R.layout.fragment_edit_create_product_list) {
    private val viewModel: EditCreateProductListViewModel by viewModels()
    private var _binding: FragmentEditCreateProductListBinding? = null
    private val binding get() = _binding!!
    private val args: ProductsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditCreateProductListBinding.bind(view)

        if (args.productListId.isNotBlank()) {
            viewModel.getProductListDetail(args.productListId)
            binding.deleteProductListBtn.visibility = View.VISIBLE
        }

        setClickListeners()
        setObservers()
    }

    private fun setClickListeners() {
        with(binding) {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            deleteProductListBtn.setOnClickListener {
                viewModel.deleteProductList()
            }

            addProductListImg.setOnClickListener {
                viewModel.changeProductListName(binding.nameProductListEt.text.toString())
            }

            deleteImgBtn.setOnClickListener {
                viewModel.changeProductListImg("")
            }

            doneBtn.setOnClickListener {
                viewModel.changeProductListName(binding.nameProductListEt.text.toString())
                viewModel.saveProductList()
                findNavController().popBackStack()
            }
        }
    }

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(productList: ProductList) {
        with(binding) {
            nameProductListEt.setText(productList.name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}