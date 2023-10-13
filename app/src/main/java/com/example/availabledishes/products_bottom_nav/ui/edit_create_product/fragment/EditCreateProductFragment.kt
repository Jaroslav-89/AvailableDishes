package com.example.availabledishes.products_bottom_nav.ui.edit_create_product.fragment

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
import com.example.availabledishes.databinding.FragmentEditCreateProductsBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.edit_create_product.view_model.EditCreateProductViewModel
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditCreateProductFragment : Fragment() {

    private val viewModel: EditCreateProductViewModel by viewModel()
    private lateinit var binding: FragmentEditCreateProductsBinding
    private var inFavorite = false
    private var needToBuy = false
    private var isNewProduct = false
    private var productNameText: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditCreateProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireArguments().getString(NEW_PRODUCT).isNullOrEmpty()) {
            renderState(Product("", null, null, null, null, null))
            isNewProduct = true
            binding.deleteBtn.visibility = View.INVISIBLE
            binding.headingProductTv.text = context?.getString(R.string.create_new_product_heading) ?: ""
            binding.editCreateProductBtn.text = context?.getString(R.string.create_product_btn) ?: ""
        } else {
            viewModel.getProductByName(requireArguments().getString(NEW_PRODUCT)!!)
            isNewProduct = false
            binding.deleteBtn.visibility = View.VISIBLE
            binding.headingProductTv.text = context?.getString(R.string.edit_product_heading) ?: ""
            binding.editCreateProductBtn.text = context?.getString(R.string.save_product_btn) ?: ""
        }

        setTextChangedListener()

        setEditCreateButton(productNameText)

        binding.backBtn.setOnClickListener {
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
                setEditCreateButton(productNameText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.nameProductEt.addTextChangedListener(textWatcher)
    }

    private fun setEditCreateButton(text: String?) {
        if (text.isNullOrEmpty()) {
            binding.editCreateProductBtn.alpha = 0.5f
            binding.editCreateProductBtn.isEnabled = false
        } else {
            binding.editCreateProductBtn.alpha = 1f
            binding.editCreateProductBtn.isEnabled = true
        }
    }

    private fun renderState(product: Product?) {
        with(binding) {
            nameProductEt.setText(product?.name ?: "")
            descriptionProductEt.setText(product?.description ?: "")
            favoriteIc.setImageDrawable(
                getFavoriteToggleDrawable(
                    product?.inFavorite ?: inFavorite
                )
            )
            needToBuyIc.setImageDrawable(
                getNeedToBuyToggleDrawable(
                    product?.needToBuy ?: needToBuy
                )
            )
            if (!isNewProduct) {
                inFavorite = product?.inFavorite ?: false
                needToBuy = product?.needToBuy ?: false
            }

            setClickListeners(product)
        }
    }

    private fun setClickListeners(product: Product?) {
        binding.deleteBtn.setOnClickListener {
            viewModel.deleteProduct(product!!)
            findNavController().popBackStack()
            findNavController().popBackStack()
        }

        binding.editCreateProductBtn.setOnClickListener {
            if (isNewProduct) {
                viewModel.createNewProduct(
                    Product(
                        name = binding.nameProductEt.text.toString(),
                        tag = null,
                        imgUrl = null,
                        description = binding.descriptionProductEt.text.toString(),
                        inFavorite = inFavorite,
                        needToBuy = needToBuy
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
                        tag = null,
                        imgUrl = null,
                        description = binding.descriptionProductEt.text.toString(),
                        inFavorite = inFavorite,
                        needToBuy = needToBuy
                    )
                )
                findNavController().navigate(
                    R.id.action_createProduct_to_detailProduct,
                    DetailProductFragment.createArgs(binding.nameProductEt.text.toString()),
                )
            }
        }

        binding.favoriteIc.setOnClickListener {
   //         if (isNewProduct) {
                if (!inFavorite) {
                    inFavorite = true
                    binding.favoriteIc.setImageDrawable(getFavoriteToggleDrawable(inFavorite))
                } else {
                    inFavorite = false
                    binding.favoriteIc.setImageDrawable(getFavoriteToggleDrawable(inFavorite))
                    needToBuy = false
                    binding.needToBuyIc.setImageDrawable(getNeedToBuyToggleDrawable(needToBuy))
                }
//            } else {
//                viewModel.toggleFavorite(product!!)
//            }
        }

        binding.needToBuyIc.setOnClickListener {
//            if (isNewProduct) {
                if (!needToBuy) {
                    needToBuy = true
                    binding.needToBuyIc.setImageDrawable(getNeedToBuyToggleDrawable(needToBuy))
                    inFavorite = true
                    binding.favoriteIc.setImageDrawable(getFavoriteToggleDrawable(inFavorite))
                } else {
                    needToBuy = false
                    binding.needToBuyIc.setImageDrawable(getNeedToBuyToggleDrawable(needToBuy))
                }
//            } else {
//                viewModel.toggleNeedToBuy(product!!)
//            }
        }
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {
        return context?.getDrawable(
            if (inFavorite == null || !inFavorite) R.drawable.ic_inactive_favorite else R.drawable.ic_active_favorite
        )
    }

    private fun getNeedToBuyToggleDrawable(needToBuy: Boolean?): Drawable? {
        return context?.getDrawable(
            if (needToBuy == null || !needToBuy) R.drawable.ic_inactive_need_to_buy else R.drawable.ic_need_to_buy
        )
    }

    companion object {
        private const val NEW_PRODUCT = "new_product"

        fun createArgs(productName: String?): Bundle =
            bundleOf(NEW_PRODUCT to productName)
    }
}