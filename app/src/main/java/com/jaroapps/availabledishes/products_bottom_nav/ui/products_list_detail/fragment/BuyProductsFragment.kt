package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.FragmentBuyProductsBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.ui.product_detail.fragment.DetailProductFragment
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.adapter.MyProductsAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model.BuyProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyProductsFragment : Fragment() {

    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private val viewModel: BuyProductsViewModel by viewModels()
    private var _binding: FragmentBuyProductsBinding? = null
    private val binding get() = _binding!!
    private val adapter = MyProductsAdapter(
        object : MyProductsAdapter.MyProductClickListener {
            override fun onProductClick(product: Product) {
                findNavController().navigate(
                    R.id.action_productsFragment_to_detailProduct,
                    DetailProductFragment.createArgs(product.name)
                )
            }

            override fun onFavoriteToggleClick(product: Product) {
                viewModel.toggleFavorite(product)
            }

            override fun onBuyToggleClick(product: Product) {
                viewModel.toggleBuy(product)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        viewModel.getBuyProductsList()

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

    private fun setAdapter() {
        binding.buyProductsRv.adapter = adapter
        val itemAnimator = binding.buyProductsRv.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun renderState(productsList: List<Product>) {
        adapter.setProductsList(productsList)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBuyProductsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = BuyProductsFragment()
    }
}