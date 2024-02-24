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
import com.example.availabledishes.databinding.FragmentMyProductsBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.add_products.fragment.AddProductsFragment
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import com.example.availabledishes.products_bottom_nav.ui.my_products.adapter.MyProductsAdapter
import com.example.availabledishes.products_bottom_nav.ui.my_products.view_model.MyProductsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyProductsFragment : Fragment() {

    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val viewModel: MyProductsViewModel by viewModel()
    private var _binding: FragmentMyProductsBinding? = null
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
                if (product.needToBuy != true) {
                    viewModel.toggleBuy(product)
                }
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myProductsRv.adapter = adapter

        viewModel.getMyProductsList()

        binding.addProducts.setOnClickListener {
            findNavController().navigate(
                R.id.action_productsFragment_to_addProductsFragment,
                AddProductsFragment.createArgs(null)
            )
        }

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        setExitConfirmDialog()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    confirmDialog.show()
//                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
//                        backToast.cancel()
//                        requireActivity().finish()
//                        return
//                    } else {
//                        Toast.makeText(
//                            requireContext(),
//                            requireContext()?.getString(R.string.double_back),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    backPressedTime = System.currentTimeMillis()
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
        viewModel.getMyProductsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setExitConfirmDialog(){
        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog)
            .setTitle("Закрыть приложение?")
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Нет") { dialog, which ->

            }.setPositiveButton("Да") { dialog, which ->
                requireActivity().finish()
            }
    }

    companion object {
        fun newInstance() = MyProductsFragment()
    }
}