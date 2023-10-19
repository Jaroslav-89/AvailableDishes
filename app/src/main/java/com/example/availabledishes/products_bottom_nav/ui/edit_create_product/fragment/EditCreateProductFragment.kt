package com.example.availabledishes.products_bottom_nav.ui.edit_create_product.fragment

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
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
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
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

    private val keyboard by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val tagAdapter = CreateEditTagAdapter(
        object : CreateEditTagAdapter.DeleteTagListener {
            override fun onTagClick(tag: ProductTag) {
                hideKeyboard()
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

        viewModel.nameAvailable.observe(viewLifecycleOwner) {
            saveProductAndRedirect(it)
        }

        setClickListeners()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    alertDialog(BACK)
                }
            })
    }

    private fun initView() {
        if (requireArguments().getString(AVAILABLE_PRODUCT).isNullOrEmpty()) {
            viewModel.prepareNewProduct()
            binding.deleteProductBtn.visibility = View.GONE
            binding.headingProductTv.text =
                context?.getString(R.string.create_new_product_heading)
        } else {
            viewModel.getProductByName(requireArguments().getString(AVAILABLE_PRODUCT)!!)
            binding.deleteProductBtn.visibility = View.VISIBLE
            binding.headingProductTv.text =
                requireContext().getString(R.string.edit_product_heading)
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
            nameProductEt.setSelection(nameProductEt.text.length)
            product.tag?.let { tagAdapter.setTagsList(it) }
            descriptionProductEt.setText(product.description)
            descriptionProductEt.setSelection(descriptionProductEt.text.length)
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
            hideKeyboard()
            alertDialog(BACK)
        }

        binding.deleteProductBtn.setOnClickListener {
            alertDialog(DELETE_PRODUCT)
        }

        binding.addProductImg.setOnClickListener {
            saveNameAndDescriptionInViewModel()
            dispatchPickImageIntent()
        }

        binding.deleteImgBtn.setOnClickListener {
            hideKeyboard()
            alertDialog(DELETE_IMG)
        }

        binding.addTagBtn.setOnClickListener {
            hideKeyboard()
            saveNameAndDescriptionInViewModel()
            binding.addTagScreen.visibility = View.VISIBLE
        }

        binding.addTagScreen.setOnClickListener {
            binding.addTagScreen.visibility = View.GONE
        }

        binding.doneBtn.setOnClickListener {
            hideKeyboard()
            saveNameAndDescriptionInViewModel()
            if (binding.nameProductEt.text.isBlank()) {
                showToast(context?.getString(R.string.empty_product_name)!!)
                return@setOnClickListener
            }
            viewModel.checkingNameNewProductForMatches()
        }

        binding.favoriteIc.setOnClickListener {
            saveNameAndDescriptionInViewModel()
            viewModel.toggleFavorite()
        }

        binding.needToBuyIc.setOnClickListener {
            saveNameAndDescriptionInViewModel()
            viewModel.toggleNeedToBuy()
        }

        binding.productEditCreateGroup.setOnClickListener {
            binding.nameProductEt.clearFocus()
            binding.descriptionProductEt.clearFocus()
            keyboard.hideSoftInputFromWindow(binding.nameProductEt.windowToken, 0)
            keyboard.hideSoftInputFromWindow(binding.descriptionProductEt.windowToken, 0)
        }
    }

    private fun hideKeyboard() {
        binding.nameProductEt.clearFocus()
        binding.descriptionProductEt.clearFocus()
        keyboard.hideSoftInputFromWindow(binding.nameProductEt.windowToken, 0)
        keyboard.hideSoftInputFromWindow(binding.descriptionProductEt.windowToken, 0)
    }

    private fun saveProductAndRedirect(nameAvailable: Boolean) {
        if (nameAvailable) {
            if (requireArguments().getString(AVAILABLE_PRODUCT).isNullOrEmpty()) {
                viewModel.createNewProduct()
                findNavController().popBackStack(
                    R.id.addProductsFragment,
                    false
                )
            } else {
                alertDialog(SAVE)
            }
        } else {
            showToast(requireContext().getString(R.string.already_exists))
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
            binding.deleteImgBtn.visibility = View.VISIBLE
        } else {
            binding.deleteImgBtn.visibility = View.GONE
        }

        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.place_holder_product_new)
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
            viewModel.editPlaceHolderImg(imageUri.toString())
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

    private fun alertDialog(state: String) {
        var title = ""
        var message = ""
        var positive = ""
        var negative = ""

        when (state) {
            SAVE -> {
                title = requireContext().getString(R.string.title_save)
                message = requireContext().getString(R.string.message_save)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }

            BACK -> {
                title = requireContext().getString(R.string.title_back)
                message = requireContext().getString(R.string.message_back)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }

            DELETE_PRODUCT -> {
                title = requireContext().getString(R.string.title_delete)
                message = requireContext().getString(R.string.message_delete_product)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }

            DELETE_IMG -> {
                title = requireContext().getString(R.string.title_delete)
                message = requireContext().getString(R.string.message_delete_img)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positive) { dialogInterface: DialogInterface, i: Int ->
            when (state) {
                SAVE -> {
                    viewModel.changeProduct()
                    findNavController().navigate(
                        R.id.action_createProduct_to_detailProduct,
                        DetailProductFragment.createArgs(binding.nameProductEt.text.toString()),
                    )
                }

                BACK -> {
                    findNavController().popBackStack()
                }

                DELETE_PRODUCT -> {
                    viewModel.deleteProduct()
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }

                DELETE_IMG -> {
                    viewModel.deleteProductImg()
                }
            }

        }

        builder.setNegativeButton(negative) { dialogInterface: DialogInterface, i: Int ->
            return@setNegativeButton
        }

        val alertDialog = builder.create()
        alertDialog.show()

        val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(requireContext().getColor(R.color.dark_gray))

        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(requireContext().getColor(R.color.dark_gray))
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val AVAILABLE_PRODUCT = "new_product"
        private const val REQUEST_IMAGE_PICK = 2

        private const val DELETE_PRODUCT = "delete_product"
        private const val DELETE_IMG = "delete_img"
        private const val BACK = "back"
        private const val SAVE = "save"

        fun createArgs(productName: String?): Bundle =
            bundleOf(AVAILABLE_PRODUCT to productName)
    }
}
