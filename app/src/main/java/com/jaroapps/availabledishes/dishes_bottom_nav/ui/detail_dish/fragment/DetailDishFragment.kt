package com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.fragment

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.databinding.FragmentDetailDishBinding
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.adapter.DetailDishProductsAdapter
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.adapter.DetailDishTagAdapter
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.view_model.DetailDishViewModel
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.edit_create_dish.fragment.EditeCreateDishFragment
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.fragment.AllDishesFragment
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailDishFragment : Fragment() {

    private val viewModel: DetailDishViewModel by viewModels()
    private lateinit var binding: FragmentDetailDishBinding
    private var dishName = ""
    private var productsListVisible = false
    private var recipeVisible = false

    private val tagAdapter = DetailDishTagAdapter(
        object : DetailDishTagAdapter.DetailDishTagListener {
            override fun onTagClick(tag: Tag) {
                findNavController().navigate(
                    R.id.action_detailDishFragment_to_dishesFragment,
                    AllDishesFragment.createArgs(tag.name)
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

        getDishNameFromArguments()
        getDishByName()
        setAdapters()
        setClickListeners()
        setObservers()
    }

    private fun getDishNameFromArguments() {
        dishName = requireArguments().getString(ARGS_DISH) ?: ""
    }

    private fun getDishByName() {
        viewModel.getDishByName(dishName)
    }

    private fun setAdapters() {
        binding.tagsRv.adapter = tagAdapter
        binding.productListRv.adapter = productAdapter
    }

    private fun setClickListeners() {
        with(binding) {
            back.setOnClickListener {
                findNavController().navigate(
                    R.id.action_detailDishFragment_to_dishesFragment
                )
                //findNavController().navigateUp()
            }

            favorite.setOnClickListener {
                viewModel.toggleFavorite()
                //  viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)
            }

            editDish.setOnClickListener {
                findNavController().navigate(
                    R.id.action_detailDishFragment_to_editeCreateDishFragment,
                    EditeCreateDishFragment.createArgs(dishName)
                )
            }

            availableProductsBtn.setOnClickListener {
                if (!productsListVisible) {
                    productsListVisible = true
                    arrowProducts.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_arrow_up
                        )
                    )
                    availableProductsGroup.visibility = View.VISIBLE
                } else {
                    productsListVisible = false
                    arrowProducts.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_arrow_down
                        )
                    )
                    availableProductsGroup.visibility = View.GONE
                }
            }

            addProductsInBuyListBtn.setOnClickListener {
//                    for (product in dish.ingredients) {
//                        if (product.inFavorite != true) {
//                            viewModel.toggleFavoriteProduct()
//                        }
//                    }
//
//                viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)
            }

            recipeBtn.setOnClickListener {
                if (!recipeVisible) {
                    recipeVisible = true
                    arrowRecipe.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_arrow_up
                        )
                    )
                    recipeDish.visibility = View.VISIBLE
                } else {
                    recipeVisible = false
                    arrowRecipe.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_arrow_down
                        )
                    )
                    recipeDish.visibility = View.GONE
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        viewModel.tagList.observe(viewLifecycleOwner) {
            renderTags(it)
        }

        viewModel.dishIngredients.observe(viewLifecycleOwner) {
            renderDishIngredients(it)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        viewModel.getDishByName(requireArguments().getString(ARGS_DISH)!!)
//    }

    private fun renderState(dish: Dish) {
        with(binding) {
            headingDish.text = dish.name
            setPlaceHolderDrawable(dish.imgUrl.toUri())
            favorite.setImageDrawable(getFavoriteToggleDrawable(dish.inFavorite))
            descriptionDish.text = dish.description
            recipeDish.text = dish.recipe

        }
    }

    private fun renderTags(tagList: List<Tag>) {
        tagAdapter.setTagsList(tagList)
    }

    private fun renderDishIngredients(products: List<Product>) {
        productAdapter.setProductsList(products)
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {

        return if (inFavorite == null || !inFavorite) {
            getDrawable(requireContext(), R.drawable.ic_inactive_favorite_detail_product)
        } else {
            getDrawable(requireContext(), R.drawable.ic_active_favorite_detail_product)
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