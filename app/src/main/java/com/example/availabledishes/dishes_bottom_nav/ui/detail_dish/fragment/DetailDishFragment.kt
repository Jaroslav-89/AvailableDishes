package com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.fragment

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentDetailDishBinding
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.dishes_bottom_nav.domain.model.DishTag
import com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.adapter.DetailDishProductsAdapter
import com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.adapter.DetailDishTagAdapter
import com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.view_model.DetailDishViewModel
import com.example.availabledishes.dishes_bottom_nav.ui.list_dishes.fragment.AllDishes
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailDishFragment : Fragment() {

    private val viewModel: DetailDishViewModel by viewModel()
    private lateinit var binding: FragmentDetailDishBinding

    private var productsListVisible = false
    private var recipeVisible = false

    private val tagAdapter = DetailDishTagAdapter(
        object : DetailDishTagAdapter.DetailDishTagListener {
            override fun onTagClick(tag: DishTag) {
                findNavController().navigate(
                    R.id.action_detailDishFragment_to_dishesFragment,
                        AllDishes.createArgs(tag.name)
                )
            }
        }
    )

    private val productAdapter = DetailDishProductsAdapter(
        object : DetailDishProductsAdapter.ProductClickListener {

            override fun onProductClick(product: Product) {
                findNavController().navigate(
                    R.id.action_detailDishFragment_to_detailProduct,
                      DetailProductFragment.createArgs(product.name)
                )
            }

            override fun onFavoriteToggleClick(product: Product) {
                viewModel.toggleFavoriteProduct(product)
                viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)
            }

            override fun onBuyToggleClick(product: Product) {
                viewModel.toggleBuyProduct(product)
                viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tagsRv.adapter = tagAdapter
        binding.productListRv.adapter = productAdapter

        viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)
    }

    private fun renderState(dish: Dish) {
        with(binding) {
            headingDish.text = dish.name
            setPlaceHolderDrawable(dish.imgUrl?.toUri())
            favorite.setImageDrawable(getFavoriteToggleDrawable(dish.inFavorite))
            dish.tag?.let { tagAdapter.setTagsList(it) }
            tagAdapter.notifyDataSetChanged()
            descriptionDish.text = dish.description ?: ""
            dish.ingredients?.let { productAdapter.setProductsList(it) }
            productAdapter.notifyDataSetChanged()
            recipeDish.text = dish.recipe

            setClickListeners(dish)
        }
    }

    private fun setClickListeners(dish: Dish) {
        with(binding) {
            favorite.setOnClickListener {
                viewModel.toggleFavorite(dish)
                viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)
            }

            editDish.setOnClickListener {
//                findNavController().navigate(
//                    R.id.action_detailProduct_to_createProduct,
//                    EditCreateProductFragment.createArgs(product.name)
//                )
            }

            availableProductsBtn.setOnClickListener {
                if (!productsListVisible) {
                    productsListVisible = true
                    arrowProducts.setImageDrawable(context?.getDrawable(R.drawable.ic_arrow_up))
                    availableProductsGroup.visibility = View.VISIBLE
                } else {
                    productsListVisible = false
                    arrowProducts.setImageDrawable(context?.getDrawable(R.drawable.ic_arrow_down))
                    availableProductsGroup.visibility = View.GONE
                }
            }

            addProductsInBuyListBtn.setOnClickListener {
                if (dish.ingredients != null) {
                    for (product in dish.ingredients) {
                        if (product.inFavorite != true) {
                            viewModel.toggleFavoriteProduct(product)
                        }
                    }
                }
                viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)
            }

            recipeBtn.setOnClickListener {
                if (!recipeVisible) {
                    recipeVisible = true
                    arrowRecipe.setImageDrawable(context?.getDrawable(R.drawable.ic_arrow_up))
                    recipeDish.visibility = View.VISIBLE
                } else {
                    recipeVisible = false
                    arrowRecipe.setImageDrawable(context?.getDrawable(R.drawable.ic_arrow_down))
                    recipeDish.visibility = View.GONE
                }
            }
        }
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {

        if (inFavorite == null || !inFavorite) {
            return context?.getDrawable(R.drawable.ic_inactive_favorite_detail_product)
        } else {
            return context?.getDrawable(R.drawable.ic_active_favorite_detail_product)
        }
    }


    private fun setPlaceHolderDrawable(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.place_holder_dish)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.corner_radius)
                ),
            )
            .into(binding.placeHolderDish)
    }

    companion object {
        private const val ARGS_DISH = "dish"

        fun createArgs(dishName: String): Bundle =
            bundleOf(ARGS_DISH to dishName)
    }
}