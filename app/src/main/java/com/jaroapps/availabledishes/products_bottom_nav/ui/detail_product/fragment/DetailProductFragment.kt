package com.jaroapps.availabledishes.products_bottom_nav.ui.detail_product.fragment

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.databinding.FragmentDetailProductBinding
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.fragment.DetailDishFragment
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.ui.add_products.fragment.AddProductsFragment
import com.jaroapps.availabledishes.products_bottom_nav.ui.detail_product.adapter.AvailableDishesAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.detail_product.adapter.DetailProductTagAdapter
import com.jaroapps.availabledishes.products_bottom_nav.ui.detail_product.view_model.DetailProductViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product.fragment.EditCreateProductFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailProductFragment : Fragment() {

    private val viewModel: DetailProductViewModel by viewModel()
    private lateinit var binding: FragmentDetailProductBinding
    private var availableDishesVisible = false
    private var productName = ""

    private val tagAdapter = DetailProductTagAdapter(
        object : DetailProductTagAdapter.DetailProductTagListener {
            override fun onTagClick(tag: Tag) {
                findNavController().navigate(
                    R.id.action_detailProduct_to_addProductsFragment,
                    AddProductsFragment.createArgs(tag.name)
                )
            }
        }
    )

    private val availableDishesAdapter = AvailableDishesAdapter(
        object : AvailableDishesAdapter.DishClickListener {
            override fun onDishClick(dish: Dish) {
                findNavController().navigate(
                    R.id.action_detailProduct_to_detailDishFragment,
                    DetailDishFragment.createArgs(dish.name)
                )
            }

            override fun onDishFavoriteToggleClick(dish: Dish) {
                viewModel.toggleDishFavorite(dish)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProductNameFromArguments()
        getProductByName()
        setAdapters()
        setClickListeners()
        setObservers()
    }

    private fun getProductNameFromArguments() {
        productName = requireArguments().getString(ARGS_PRODUCT) ?: ""
    }

    private fun getProductByName() {
        viewModel.getProductByName(productName)
    }

    private fun setAdapters() {
        binding.tagsRv.adapter = tagAdapter
        binding.availableDishesRv.adapter = availableDishesAdapter
    }

    private fun setClickListeners() {
        with(binding) {
            back.setOnClickListener {
                findNavController().navigateUp()
            }

            favorite.setOnClickListener {
                viewModel.toggleFavorite()
            }

            needToBuy.setOnClickListener {
                viewModel.toggleNeedToBuy()
            }

            editProduct.setOnClickListener {
                findNavController().navigate(
                    R.id.action_detailProduct_to_createProduct,
                    EditCreateProductFragment.createArgs(productName)
                )
            }

            availableDishesBtn.setOnClickListener {
                if (!availableDishesVisible) {
                    availableDishesVisible = true
                    arrow.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_arrow_up))
                    availableDishesGroup.visibility = View.VISIBLE
                    placeHolderRecyclerView.visibility = View.GONE
                  //  viewModel.getAllDishesWithThisProduct()
                } else {
                    availableDishesVisible = false
                    arrow.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_arrow_down))
                    availableDishesGroup.visibility = View.GONE
                    placeHolderRecyclerView.visibility = View.VISIBLE
                }
            }

            createDishBtn.setOnClickListener {
                //todo оздать блюдо с этим рецептом
            }
        }
    }

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        viewModel.tagList.observe(viewLifecycleOwner) {
            renderTag(it)
        }

        viewModel.dishesListWithProduct.observe(viewLifecycleOwner) {
            renderAvailableDishes(it)
        }
    }

    private fun renderState(product: Product) {
        with(binding) {
            setPlaceHolderDrawable(product.imgUrl.toUri())
            headingProduct.text = product.name
            favorite.setImageDrawable(getFavoriteToggleDrawable(product.inFavorite))
            needToBuy.setImageDrawable(getNeedToBueToggleDrawable(product.needToBuy))
            descriptionProduct.text = product.description
        }
    }

    private fun renderTag(tagList: List<Tag>) {
        tagAdapter.setTagsList(tagList)
    }

    private fun renderAvailableDishes(dishList: List<Dish>) {
        availableDishesAdapter.setDishesList(dishList)
    }


    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {

        return if (inFavorite == null || !inFavorite) {
            binding.needToBuy.visibility = View.GONE
            getDrawable(requireContext(), R.drawable.ic_inactive_favorite_detail_product)
        } else {
            binding.needToBuy.visibility = View.VISIBLE
            getDrawable(requireContext(), R.drawable.ic_active_favorite_detail_product)
        }
    }

    private fun getNeedToBueToggleDrawable(needToBuy: Boolean?): Drawable? {
        return if (needToBuy == null || !needToBuy) {
            getDrawable(requireContext(), R.drawable.ic_inactive_need_to_buy_detail_product)
        } else {
            getDrawable(requireContext(), R.drawable.ic_need_to_buy_detail_product)
        }
    }

    private fun setPlaceHolderDrawable(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.place_holder_product_new)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.corner_radius)
                ),
            )
            .into(binding.placeHolderProduct)
    }

    companion object {
        private const val ARGS_PRODUCT = "product"

        fun createArgs(productName: String): Bundle =
            bundleOf(ARGS_PRODUCT to productName)
    }
}