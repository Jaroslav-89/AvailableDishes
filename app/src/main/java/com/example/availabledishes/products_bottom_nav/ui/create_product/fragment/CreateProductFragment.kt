package com.example.availabledishes.products_bottom_nav.ui.create_product.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentCreateProductsBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.create_product.view_model.CreateProductViewModel
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateProductFragment : Fragment() {

    private val viewModel: CreateProductViewModel by viewModel()
    private lateinit var binding: FragmentCreateProductsBinding
    private var inFavorite = false
    private var isNewProduct = false
    private var productNameText: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireArguments().getString(NEW_PRODUCT).isNullOrEmpty()) {
            renderState(Product("", null, null, null))
            isNewProduct = true
        } else {
            viewModel.getProductByName(requireArguments().getString(NEW_PRODUCT)!!)
            isNewProduct = false
        }

        setTextChangedListener()

        setCreateButton(productNameText)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun setTextChangedListener() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productNameText = s.toString()
                setCreateButton(productNameText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.nameProductEt.addTextChangedListener(textWatcher)
    }

    private fun setCreateButton(text: String?) {
        if (text.isNullOrEmpty()) {
            binding.createProductBtn.alpha = 0.5f
            binding.createProductBtn.isClickable = false
        } else {
            binding.createProductBtn.alpha = 1f
            binding.createProductBtn.isClickable = true
        }
    }

    private fun renderState(product: Product?) {
        with(binding) {
            nameProductEt.setText(product?.name ?: "")
            descriptionProductEt.setText(product?.description ?: "")
            favorite.setImageDrawable(getFavoriteToggleDrawable(product?.inFavorite ?: false))
            inFavorite = product?.inFavorite ?: false

            setClickListeners(product)
        }
    }

    private fun setClickListeners(product: Product?) {
        binding.createProductBtn.setOnClickListener {
            if (isNewProduct) {
                viewModel.createNewProduct(
                    Product(
                        name = binding.nameProductEt.text.toString(),
                        description = binding.descriptionProductEt.text.toString(),
                        inFavorite = inFavorite,
                        needToBuy = false
                    )
                )
                findNavController().popBackStack(
                    R.id.addProductsFragment,
                    false
                )
            } else {
                viewModel.changeProduct(
                    product!!, Product(
                        name = binding.nameProductEt.text.toString(),
                        description = binding.descriptionProductEt.text.toString(),
                        inFavorite = product.inFavorite,
                        needToBuy = product.needToBuy
                    )
                )
                findNavController().navigate(
                    R.id.action_createProduct_to_detailProduct,
                    DetailProductFragment.createArgs(binding.nameProductEt.text.toString()),
                )
            }
        }

        binding.favorite.setOnClickListener {
            viewModel.toggleFavorite(product!!)
        }
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {
        return context?.getDrawable(
            if (inFavorite == null || !inFavorite) R.drawable.ic_inactive_favorite else R.drawable.ic_active_favorite
        )
    }

    companion object {
        private const val NEW_PRODUCT = "new_product"

        fun createArgs(productName: String?): Bundle =
            bundleOf(NEW_PRODUCT to productName)
    }
}