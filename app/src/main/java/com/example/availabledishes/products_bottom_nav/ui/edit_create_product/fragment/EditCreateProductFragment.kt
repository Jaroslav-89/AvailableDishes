package com.example.availabledishes.products_bottom_nav.ui.edit_create_product.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
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
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.Locale

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
                viewModel.toggleTag(tag, true)
            }
        }
    )

    private val addTagAdapter = AddTagAdapter(
        object : AddTagAdapter.AddTagListener {
            override fun onTagClick(tag: ProductTag) {
                viewModel.toggleTag(tag, false)
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

        //setEditCreateButton(productNameText)

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
//                productNameText = s.toString()
//                setEditCreateButton(productNameText)

            }

            override fun afterTextChanged(s: Editable?) {
                //Это условие для того, чтобы использовать один textWatcher для двух editText
                if (s == binding.nameProductEt.editableText) {
                    viewModel.editNameText(binding.nameProductEt.text.toString())
                } else if (s == binding.descriptionProductEt.editableText) {
                    viewModel.editDescriptionText(binding.nameProductEt.text.toString())
                }
            }
        }
        binding.nameProductEt.addTextChangedListener(textWatcher)
        binding.nameProductEt.addTextChangedListener(textWatcher)
    }

//    private fun setEditCreateButton(text: String?) {
//        if (text.isNullOrEmpty()) {
//            binding.editCreateProductBtn.alpha = 0.5f
//            binding.editCreateProductBtn.isEnabled = false
//        } else {
//            binding.editCreateProductBtn.alpha = 1f
//            binding.editCreateProductBtn.isEnabled = true
//        }
//    }

    private fun renderState(product: Product) {
        with(binding) {
            setImgFromPlaceHolder(product.imgUrl?.toUri())
            product.tag?.let { tagAdapter.setTagsList(it) }
            if (binding.nameProductEt.text.toString().isBlank()) {
                nameProductEt.setText(product.name)
            }
            if (binding.descriptionProductEt.text.toString().isBlank()) {
                descriptionProductEt.setText(product.description)
            }
            favoriteIc.setImageDrawable(
                getFavoriteToggleDrawable(
                    product.inFavorite
                )
            )
            needToBuyIc.setImageDrawable(
                getNeedToBuyToggleDrawable(
                    product.needToBuy
                )
            )
            if (!isNewProduct) {
                inFavorite = product.inFavorite!!
                needToBuy = product.needToBuy!!
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

        binding.addTagScreen.setOnClickListener {
            binding.addTagScreen.visibility = View.GONE
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
                            name = product.name,
                            imgUrl = product.imgUrl,
                            tag = product.tag,
                            description = product.description,
                            inFavorite = product.inFavorite,
                            needToBuy = product.needToBuy
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
            viewModel.toggleFavorite()
        }

        binding.needToBuyIc.setOnClickListener {
            viewModel.toggleNeedToBuy()
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

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_PICK) {
            imageUri = data?.data ?: imageUri
            viewModel.editPlaceHolderImg(imageUri.toString(), false)
        }
    }

    private fun dispatchPickImageIntent() {
        // Create an intent with action as ACTION_PICK
        val pickImageIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                // Set the type as image
                type = "image/*"
            }

        // Create an intent with action as ACTION_IMAGE_CAPTURE
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            // Ensure that there's a camera activity to handle the intent
            resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.availabledishes.fileprovider",
                        it
                    )
                    putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
            }
        }

        // Start the intent to get the image
        val chooser = Intent.createChooser(pickImageIntent, "Select or take a new picture")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePhotoIntent))
        startActivityForResult(chooser, REQUEST_IMAGE_PICK)
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            // Save a file: path for use with ACTION_VIEW intents
            imageUri = Uri.fromFile(this)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val NEW_PRODUCT = "new_product"
        private const val REQUEST_IMAGE_PICK = 2

        fun createArgs(productName: String?): Bundle =
            bundleOf(NEW_PRODUCT to productName)
    }
}
