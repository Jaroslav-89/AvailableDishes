package com.example.availabledishes.products_bottom_nav.ui.edit_create_product.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.availabledishes.R
import com.example.availabledishes.databinding.FragmentEditCreateProductsBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag
import com.example.availabledishes.products_bottom_nav.ui.detail_product.fragment.DetailProductFragment
import com.example.availabledishes.products_bottom_nav.ui.edit_create_product.adapter.AddTagAdapter
import com.example.availabledishes.products_bottom_nav.ui.edit_create_product.adapter.CreateEditTagAdapter
import com.example.availabledishes.products_bottom_nav.ui.edit_create_product.view_model.EditCreateProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditCreateProductFragment : Fragment() {

    private val viewModel: EditCreateProductViewModel by viewModel()
    private lateinit var binding: FragmentEditCreateProductsBinding
    private var inFavorite = false
    private var needToBuy = false
    private var isNewProduct = false
    private var productNameText: String? = null
    private var imageUri: Uri? = null

    private val tagAdapter = CreateEditTagAdapter(
        object : CreateEditTagAdapter.DeleteTagListener {
            override fun onTagClick(tag: ProductTag) {
                viewModel.deleteTag(tag)
            }
        }
    )

    private val addTagAdapter = AddTagAdapter(
        object : AddTagAdapter.AddTagListener {
            override fun onTagClick(tag: ProductTag) {
                viewModel.addTag(tag)
                binding.addTagScreen.visibility = View.GONE
            }
        }
    )

    private val staggeredGridLayoutManager = StaggeredGridLayoutManager(
        3, // количество столбцов или строк
        VERTICAL// ориентация (VERTICAL или HORIZONTAL)
    )

    private val staggeredGridLM = StaggeredGridLayoutManager(
        3, // количество столбцов или строк
        VERTICAL// ориентация (VERTICAL или HORIZONTAL)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCreateProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tagsRv.layoutManager = staggeredGridLayoutManager
        binding.tagsRv.adapter = tagAdapter
        binding.addTagRv.layoutManager = staggeredGridLM
        binding.addTagRv.adapter = addTagAdapter

        if (requireArguments().getString(NEW_PRODUCT).isNullOrEmpty()) {
            viewModel.prepareNewProduct()
            isNewProduct = true
            binding.deleteBtn.visibility = View.INVISIBLE
            binding.headingProductTv.text =
                context?.getString(R.string.create_new_product_heading) ?: ""
            binding.editCreateProductBtn.text =
                context?.getString(R.string.create_product_btn) ?: ""
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

        binding.addTagScreen.setOnClickListener {
            binding.addTagScreen.visibility = View.GONE
        }
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

    private fun renderState(product: Product) {
        with(binding) {
            setImgFromPlaceHolder(product.imgUrl?.toUri())
            product.tag?.let { tagAdapter.setTagsList(it) }
            nameProductEt.setText(product.name)
            descriptionProductEt.setText(product.description ?: "")
            favoriteIc.setImageDrawable(
                getFavoriteToggleDrawable(
                    product.inFavorite ?: inFavorite
                )
            )
            needToBuyIc.setImageDrawable(
                getNeedToBuyToggleDrawable(
                    product.needToBuy ?: needToBuy
                )
            )
            if (!isNewProduct) {
                inFavorite = product.inFavorite ?: false
                needToBuy = product.needToBuy ?: false
            }

            setClickListeners(product)
            tagAdapter.notifyDataSetChanged()
        }
    }

    private fun setClickListeners(product: Product?) {
        binding.deleteBtn.setOnClickListener {
            viewModel.deleteProduct(product!!)
            findNavController().popBackStack()
            findNavController().popBackStack()
        }

        binding.productImage.setOnClickListener {
            dispatchPickImageIntent()
        }

        binding.addTagBtn.setOnClickListener {
            binding.addTagScreen.visibility = View.VISIBLE
        }

        binding.editCreateProductBtn.setOnClickListener {
            if (isNewProduct) {
                if (product != null) {
                    viewModel.createNewProduct(
                        Product(
                            name = binding.nameProductEt.text.toString(),
                            imgUrl = imageUri.toString(),
                            tag = product.tag,
                            description = binding.descriptionProductEt.text.toString(),
                            inFavorite = inFavorite,
                            needToBuy = needToBuy
                        )
                    )
                }
                findNavController().popBackStack(
                    R.id.addProductsFragment,
                    false
                )
            } else {
                if (product != null) {
                    viewModel.changeProduct(
                        Product(
                            name = binding.nameProductEt.text.toString(),
                            imgUrl = imageUri.toString(),
                            tag = product.tag,
                            description = binding.descriptionProductEt.text.toString(),
                            inFavorite = inFavorite,
                            needToBuy = needToBuy
                        )
                    )
                }
                findNavController().navigate(
                    R.id.action_createProduct_to_detailProduct,
                    DetailProductFragment.createArgs(binding.nameProductEt.text.toString()),
                )
            }
        }

        binding.favoriteIc.setOnClickListener {
            if (!inFavorite) {
                inFavorite = true
                binding.favoriteIc.setImageDrawable(getFavoriteToggleDrawable(inFavorite))
            } else {
                inFavorite = false
                binding.favoriteIc.setImageDrawable(getFavoriteToggleDrawable(inFavorite))
                needToBuy = false
                binding.needToBuyIc.setImageDrawable(getNeedToBuyToggleDrawable(needToBuy))
            }
        }

        binding.needToBuyIc.setOnClickListener {
            if (!needToBuy) {
                needToBuy = true
                binding.needToBuyIc.setImageDrawable(getNeedToBuyToggleDrawable(needToBuy))
                inFavorite = true
                binding.favoriteIc.setImageDrawable(getFavoriteToggleDrawable(inFavorite))
            } else {
                needToBuy = false
                binding.needToBuyIc.setImageDrawable(getNeedToBuyToggleDrawable(needToBuy))
            }
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

    private fun setImgFromPlaceHolder(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.place_holder_product)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.corner_radius)
                ),
            )
            .into(binding.productImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    imageUri = data?.data
                    setImgFromPlaceHolder(imageUri)
                }
            }
        }
    }

    //todo ПЕРЕНЕСТИ ИНТЕНТ В DATA СЛОЙ
    private fun dispatchPickImageIntent() {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).also { pickImageIntent ->
            pickImageIntent.type = "image/*"
            startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK)
        }
    }

    companion object {
        private const val NEW_PRODUCT = "new_product"
        private const val REQUEST_IMAGE_PICK = 2

        fun createArgs(productName: String?): Bundle =
            bundleOf(NEW_PRODUCT to productName)
    }
}
