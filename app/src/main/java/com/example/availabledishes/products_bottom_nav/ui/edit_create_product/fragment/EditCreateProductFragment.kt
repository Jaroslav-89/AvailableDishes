package com.example.availabledishes.products_bottom_nav.ui.edit_create_product.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
    private var imageUri: Uri? = null

    private val tagAdapter = CreateEditTagAdapter(
        object : CreateEditTagAdapter.DeleteTagListener {
            override fun onTagClick(tag: ProductTag) {
                saveNameAndDescriptionInViewModel()
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

        setRvAdapters()

        initView()

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        setClickListeners()
    }

    private fun initView() {
        //   editProductName = requireArguments().getString(AVAILABLE_PRODUCT)
        if (requireArguments().getString(AVAILABLE_PRODUCT).isNullOrEmpty()) {
            viewModel.prepareNewProduct()
            binding.deleteBtn.visibility = View.GONE
            binding.headingProductTv.text =
                context?.getString(R.string.create_new_product_heading)
            binding.editCreateProductBtn.text =
                context?.getString(R.string.create_product_btn)
        } else {
            viewModel.getProductByName(requireArguments().getString(AVAILABLE_PRODUCT)!!)
            binding.deleteBtn.visibility = View.VISIBLE
            binding.headingProductTv.text = context?.getString(R.string.edit_product_heading)
            binding.editCreateProductBtn.text = context?.getString(R.string.save_product_btn)
        }
    }

    private fun setRvAdapters() {
        binding.tagsRv.layoutManager = staggeredGridLayoutManager
        binding.tagsRv.adapter = tagAdapter
        binding.addTagRv.layoutManager = staggeredGridLM
        binding.addTagRv.adapter = addTagAdapter
    }

    private fun renderState(product: Product) {
        with(binding) {
            setImgFromPlaceHolder(product.imgUrl?.toUri())
            nameProductEt.setText(product.name)
            product.tag?.let { tagAdapter.setTagsList(it) }
            descriptionProductEt.setText(product.description)
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
            tagAdapter.notifyDataSetChanged()
        }
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.deleteBtn.setOnClickListener {
            viewModel.deleteProduct()
            findNavController().popBackStack()
            findNavController().popBackStack()
        }

        binding.productImage.setOnClickListener {
            saveNameAndDescriptionInViewModel()
            dispatchPickImageIntent()
        }

        binding.addTagBtn.setOnClickListener {
            saveNameAndDescriptionInViewModel()
            binding.addTagScreen.visibility = View.VISIBLE
        }

        binding.addTagScreen.setOnClickListener {
            binding.addTagScreen.visibility = View.GONE
        }

        binding.editCreateProductBtn.setOnClickListener {
            saveNameAndDescriptionInViewModel()
            //TODO СДЕЛАТЬ ПРОВЕРКУ ДОСТУПНОСТИ НАЗВАНИЯ ПЕРЕД СОХРАНЕНИЕМ

            if (requireArguments().getString(AVAILABLE_PRODUCT).isNullOrEmpty()) {
                viewModel.createNewProduct()
                findNavController().popBackStack(
                    R.id.addProductsFragment,
                    false
                )
            } else {
                viewModel.changeProduct()
                findNavController().navigate(
                    R.id.action_createProduct_to_detailProduct,
                    DetailProductFragment.createArgs(binding.nameProductEt.text.toString()),
                )
            }
        }

        binding.favoriteIc.setOnClickListener {
            saveNameAndDescriptionInViewModel()
            viewModel.toggleFavorite()
        }

        binding.needToBuyIc.setOnClickListener {
            saveNameAndDescriptionInViewModel()
            viewModel.toggleNeedToBuy()
        }
    }

    private fun saveNameAndDescriptionInViewModel() {
        viewModel.editNameText(binding.nameProductEt.text.toString())
        viewModel.editDescriptionText(binding.descriptionProductEt.text.toString())
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
        if (uri != null) {
            //TODO ЗДЕСЬ ДЕЛАТЬ ВИДИМОЙ ИЛИ АКТИВНОЙ КНОПКУ УДАЛЕНИЯ КАРТИНКИ
        }

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
        private const val AVAILABLE_PRODUCT = "new_product"
        private const val REQUEST_IMAGE_PICK = 2

        fun createArgs(productName: String?): Bundle =
            bundleOf(AVAILABLE_PRODUCT to productName)
    }
}
