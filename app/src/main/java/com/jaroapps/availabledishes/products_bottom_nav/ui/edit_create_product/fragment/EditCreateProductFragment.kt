package com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product.fragment

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.common.ui.adapters.AddTagAdapter
import com.jaroapps.availabledishes.common.ui.adapters.CreateEditTagAdapter
import com.jaroapps.availabledishes.databinding.FragmentEditCreateProductsBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.ui.product_detail.fragment.DetailProductFragment
import com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product.view_model.EditCreateProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class  EditCreateProductFragment : Fragment() {

    private val viewModel: EditCreateProductViewModel by viewModels()
    private lateinit var binding: FragmentEditCreateProductsBinding
    private var imageUri: Uri? = null
    private var productName = ""

    private val keyboard by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val tagAdapter = CreateEditTagAdapter(
        object : CreateEditTagAdapter.DeleteTagListener {
            override fun onTagClick(tag: Tag) {
                hideKeyboard()
                saveNameAndDescriptionInViewModel()
                viewModel.toggleTag(tag, true)
            }
        }
    )

    private val addTagAdapter = AddTagAdapter(
        object : AddTagAdapter.AddTagListener {
            override fun onTagClick(tag: Tag) {
                viewModel.toggleTag(tag, false)
                binding.addTagScreen.visibility = View.GONE
            }
        }
    )

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                viewModel.editPlaceHolderImg(imageUri.toString())
            }
        }

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                viewModel.editPlaceHolderImg(imageUri.toString())
            }
        }

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

        getProductNameFromArguments()
        initView()
        setAdapters()
        setClickListeners()
        setObservers()
    }

    private fun getProductNameFromArguments() {
        productName = requireArguments().getString(AVAILABLE_PRODUCT) ?: ""
    }

    private fun initView() {
        if (productName.isEmpty()) {
            viewModel.prepareNewProduct()
            binding.deleteProductBtn.visibility = View.GONE
            binding.headingProductTv.text =
                context?.getString(R.string.create_new_product_heading)
        } else {
            viewModel.getProductByName(productName)
            binding.deleteProductBtn.visibility = View.VISIBLE
            binding.headingProductTv.text =
                requireContext().getString(R.string.edit_product_heading)
        }
    }

    private fun setAdapters() {
        binding.tagsRv.layoutManager = StaggeredGridLayoutManager(3, VERTICAL)
        binding.tagsRv.adapter = tagAdapter
        binding.addTagRv.layoutManager = StaggeredGridLayoutManager(3, VERTICAL)
        binding.addTagRv.adapter = addTagAdapter
    }

    private fun renderState(product: Product) {
        with(binding) {
            setImgFromPlaceHolder(product.imgUrl)
            nameProductEt.setText(product.name)
            nameProductEt.setSelection(nameProductEt.text.length)
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
        }
    }

    private fun renderProductTags(tagList: List<Tag>) {
        tagAdapter.setTagsList(tagList)
    }

    private fun renderAvailableProductTags(tagList: List<Tag>) {
        addTagAdapter.setTagsList(tagList)
    }

    private fun setClickListeners() {
        with(binding) {
            backBtn.setOnClickListener {
                hideKeyboard()
                alertDialog(BACK)
            }

            deleteProductBtn.setOnClickListener {
                alertDialog(DELETE_PRODUCT)
            }

            addProductImg.setOnClickListener {
                saveNameAndDescriptionInViewModel()
                showImageSourceDialog()
            }

            deleteImgBtn.setOnClickListener {
                hideKeyboard()
                alertDialog(DELETE_IMG)
            }

            addTagBtn.setOnClickListener {
                hideKeyboard()
                saveNameAndDescriptionInViewModel()
                viewModel.getAvailableProductTags()
                addTagScreen.visibility = View.VISIBLE
            }

            addTagScreen.setOnClickListener {
                addTagScreen.visibility = View.GONE
            }

            doneBtn.setOnClickListener {
                hideKeyboard()
                saveNameAndDescriptionInViewModel()
                if (nameProductEt.text.isBlank()) {
                    showToast(context?.getString(R.string.empty_product_name)!!)
                } else {
                    viewModel.checkingNameNewProductForMatches()
                }
            }

            favoriteIc.setOnClickListener {
                saveNameAndDescriptionInViewModel()
                viewModel.toggleFavorite()
            }

            needToBuyIc.setOnClickListener {
                saveNameAndDescriptionInViewModel()
                viewModel.toggleNeedToBuy()
            }

            productEditCreateGroup.setOnClickListener {
                nameProductEt.clearFocus()
                descriptionProductEt.clearFocus()
                keyboard.hideSoftInputFromWindow(nameProductEt.windowToken, 0)
                keyboard.hideSoftInputFromWindow(descriptionProductEt.windowToken, 0)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    alertDialog(BACK)
                }
            })
    }

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }

        viewModel.productTags.observe(viewLifecycleOwner) {
            renderProductTags(it)
        }

        viewModel.allTags.observe(viewLifecycleOwner) {
            renderAvailableProductTags(it)
        }

        viewModel.nameAvailable.observe(viewLifecycleOwner) {
            saveProductAndRedirect(it)
        }
    }

    private fun hideKeyboard() {
        binding.nameProductEt.clearFocus()
        binding.descriptionProductEt.clearFocus()
        keyboard.hideSoftInputFromWindow(binding.nameProductEt.windowToken, 0)
        keyboard.hideSoftInputFromWindow(binding.descriptionProductEt.windowToken, 0)
    }

    private fun showImageSourceDialog() {
        // Создаем диалоговое окно для выбора между камерой и галереей
        val options = arrayOf(
            requireContext().getString(R.string.take_picture_alert_dialog),
            requireContext().getString(R.string.pick_image_from_gallery_alert_dialog)
        )
        val builder = AlertDialog.Builder(requireContext())
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePicture()
                1 -> pickImageFromGallery()
            }
        }
        builder.show()
    }

    private fun takePicture() {
        // Создаем файл и Uri для сохранения фотографии
        imageUri = createImageUri()
        imageUri?.let { uri ->
            takePhotoLauncher.launch(uri)
        }
    }

    private fun createImageUri(): Uri? {
        // Создаем файловое имя с помощью текущего времени, чтобы избежать дублирования
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        // Получаем хранилище для фотографий
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            // Создаем файл изображения
            val file = File.createTempFile(imageFileName, ".jpg", storageDir).apply {
                imageUri = Uri.fromFile(this)
            }
            //   Получаем URI для файла с помощью FileProvider
            FileProvider.getUriForFile(
                requireContext(),
                FILE_PROVIDER,
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun pickImageFromGallery() {
        // Запускаем выбор изображения из галереи
        imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setImgFromPlaceHolder(uri: String) {
        if (uri.isNotBlank()) {
            binding.deleteImgBtn.visibility = View.VISIBLE
        } else {
            binding.deleteImgBtn.visibility = View.GONE
        }
        val newUri = uri.toUri()
        Glide.with(this)
            .load(newUri)
            .placeholder(R.drawable.place_holder_product_new)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.corner_radius)
                ),
            )
            .into(binding.productImage)
    }

    private fun saveProductAndRedirect(nameAvailable: Boolean) {
        if (nameAvailable) {
            if (productName.isBlank()) {
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
        return if (inFavorite == null || !inFavorite)
            getDrawable(requireContext(), R.drawable.ic_inactive_favorite)
        else
            getDrawable(requireContext(), R.drawable.ic_active_favorite)
    }

    private fun getNeedToBuyToggleDrawable(needToBuy: Boolean?): Drawable? {
        return if (needToBuy == null || !needToBuy)
            getDrawable(requireContext(), R.drawable.ic_inactive_need_to_buy)
        else
            getDrawable(requireContext(), R.drawable.ic_need_to_buy)
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
        private const val FILE_PROVIDER = "com.jaroapps.availabledishes.fileprovider"
        private const val AVAILABLE_PRODUCT = "new_product"
        private const val DELETE_PRODUCT = "delete_product"
        private const val DELETE_IMG = "delete_img"
        private const val BACK = "back"
        private const val SAVE = "save"

        fun createArgs(productName: String?): Bundle =
            bundleOf(AVAILABLE_PRODUCT to productName)
    }
}
