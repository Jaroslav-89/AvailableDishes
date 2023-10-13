package com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentDetailProductBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.edit_create_product.fragment.EditCreateProductFragment
import com.example.availabledishes.products_bottom_nav.ui.detail_product.view_model.DetailProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailProductFragment : Fragment() {

    private val viewModel: DetailProductViewModel by viewModel()
    private lateinit var binding: FragmentDetailProductBinding
    private var availableDishesVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductByName(requireArguments().getString(ARGS_PRODUCT)!!)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(product: Product) {
        with(binding) {
            headingProduct.text = product.name
            favorite.setImageDrawable(getFavoriteToggleDrawable(product.inFavorite))
            setPlaceHolderDrawable(product.imgUrl)
            //todo наполнение адаптера тэгов
            descriptionProduct.text = product.description ?: ""
            //todo наполнить адаптер доступных блюд

            favorite.setOnClickListener {
                viewModel.toggleFavorite(product)
            }

            editProduct.setOnClickListener {
                findNavController().navigate(
                    R.id.action_detailProduct_to_createProduct,
                    EditCreateProductFragment.createArgs(product.name)
                )
            }

            availableDishesBtn.setOnClickListener {
                if (!availableDishesVisible) {
                    availableDishesVisible = true
                    arrow.setImageDrawable(context?.getDrawable(R.drawable.ic_arrow_up))
                    availableDishesGroup.visibility = View.VISIBLE
                    placeHolderRecyclerView.visibility = View.GONE
                } else {
                    availableDishesVisible = false
                    arrow.setImageDrawable(context?.getDrawable(R.drawable.ic_arrow_down))
                    availableDishesGroup.visibility = View.GONE
                    placeHolderRecyclerView.visibility = View.VISIBLE
                }
            }

            createDishBtn.setOnClickListener {
                //todo
            }
        }
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {
        return context?.getDrawable(
            if (inFavorite == null || !inFavorite) R.drawable.ic_inactive_favorite else R.drawable.ic_active_favorite
        )
    }

    private fun setPlaceHolderDrawable(url: String?) {
        //todo использовать Glide
    }

    companion object {
        private const val ARGS_PRODUCT = "product"

        fun createArgs(productName: String): Bundle =
            bundleOf(ARGS_PRODUCT to productName)
    }
}