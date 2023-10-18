package com.example.availabledishes.products_bottom_nav.ui.my_products.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentBuyProductsBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import com.example.availabledishes.products_bottom_nav.ui.my_products.adapter.MyProductsAdapter
import com.example.availabledishes.products_bottom_nav.ui.my_products.view_model.BuyProductsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BuyProductsFragment : Fragment() {

    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private val viewModel: BuyProductsViewModel by viewModel()
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

        binding.buyProductsRv.adapter = adapter

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

    private fun renderState(productsList: List<Product>) {
        adapter.setProductsList(emptyList())
        adapter.setProductsList(productsList)
        adapter.notifyDataSetChanged()
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