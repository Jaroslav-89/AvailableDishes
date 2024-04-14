package com.jaroapps.availabledishes.dishes_bottom_nav.ui.edit_create_dish.fragment

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
import android.widget.SearchView
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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.common.ui.adapters.AddTagAdapter
import com.jaroapps.availabledishes.common.ui.adapters.CreateEditTagAdapter
import com.jaroapps.availabledishes.databinding.FragmentEditCreateDishBinding
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.fragment.DetailDishFragment
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.edit_create_dish.view_model.EditeCreateDishViewModel
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditeCreateDishFragment : Fragment() {

    private val viewModel: EditeCreateDishViewModel by viewModels()
    private lateinit var binding: FragmentEditCreateDishBinding
    private var imageUri: Uri? = null
    private var dishName = ""

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

    private val staggeredGridLayoutManager = StaggeredGridLayoutManager(
        3, // количество столбцов или строк
        RecyclerView.VERTICAL// ориентация (VERTICAL или HORIZONTAL)
    )

    private val staggeredGridLM = StaggeredGridLayoutManager(
        3, // количество столбцов или строк
        RecyclerView.VERTICAL// ориентация (VERTICAL или HORIZONTAL)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCreateDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDishNameFromArguments()
        initView()
        setAdapters()
        setListeners()
        setObservers()
    }

    private fun getDishNameFromArguments() {
        dishName = requireArguments().getString(AVAILABLE_DISH) ?: ""
    }

    private fun initView() {
        if (dishName.isEmpty()) {
            viewModel.prepareNewDish()
            binding.deleteDishBtn.visibility = View.GONE
            binding.headingDishTv.text =
                requireContext().getString(R.string.create_new_dish_heading)
        } else {
            viewModel.getProductByName(dishName)
            binding.deleteDishBtn.visibility = View.VISIBLE
            binding.headingDishTv.text =
                requireContext().getString(R.string.edit_dish_heading)
        }
    }

    private fun setAdapters() {
        binding.tagsRv.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        binding.tagsRv.adapter = tagAdapter
        binding.addTagRv.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        binding.addTagRv.adapter = addTagAdapter
    }

    private fun renderState(dish: Dish) {
        with(binding) {
            setImgFromPlaceHolder(dish.imgUrl)
            nameDishEt.setText(dish.name)
            nameDishEt.setSelection(nameDishEt.text.length)
            descriptionDishEt.setText(dish.description)
            descriptionDishEt.setSelection(descriptionDishEt.text.length)
            favoriteIc.setImageDrawable(
                getFavoriteToggleDrawable(
                    dish.inFavorite
                )
            )
        }
    }

    private fun renderDishTags(tagList: List<Tag>) {
        tagAdapter.setTagsList(tagList)
    }

    private fun renderAvailableDishTags(tagList: List<Tag>) {
        addTagAdapter.setTagsList(tagList)
    }

    private fun renderQueryProductsList(queryProducts: List<Product>) {
        // rv.setTagsList(tagList)
    }

    private fun setListeners() {
        with(binding) {
            productSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.getSearchedProducts(newText ?: "")
                    return true
                }
            })

            backBtn.setOnClickListener {
                hideKeyboard()
                alertDialog(BACK)
            }

            deleteDishBtn.setOnClickListener {
                alertDialog(DELETE_DISH)
            }

            addDishImg.setOnClickListener {
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
                viewModel.getAvailableDishTags()
                addTagScreen.visibility = View.VISIBLE
            }

            addTagScreen.setOnClickListener {
                addTagScreen.visibility = View.GONE
            }

            doneBtn.setOnClickListener {
                hideKeyboard()
                saveNameAndDescriptionInViewModel()
                if (nameDishEt.text.isBlank()) {
                    showToast(requireContext().getString(R.string.empty_dish_name))
                } else {
                    viewModel.checkingNameNewDishForMatches()
                }
            }

            favoriteIc.setOnClickListener {
                saveNameAndDescriptionInViewModel()
                viewModel.toggleFavorite()
            }

            dishEditCreateGroup.setOnClickListener {
                nameDishEt.clearFocus()
                descriptionDishEt.clearFocus()
                keyboard.hideSoftInputFromWindow(nameDishEt.windowToken, 0)
                keyboard.hideSoftInputFromWindow(descriptionDishEt.windowToken, 0)
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
            renderDishTags(it)
        }

        viewModel.allTags.observe(viewLifecycleOwner) {
            renderAvailableDishTags(it)
        }

        viewModel.queryProducts.observe(viewLifecycleOwner) {
            renderQueryProductsList(it)
        }

        viewModel.nameAvailable.observe(viewLifecycleOwner) {
            saveDishAndRedirect(it)
        }
    }

    private fun hideKeyboard() {
        binding.nameDishEt.clearFocus()
        binding.descriptionDishEt.clearFocus()
        keyboard.hideSoftInputFromWindow(binding.nameDishEt.windowToken, 0)
        keyboard.hideSoftInputFromWindow(binding.descriptionDishEt.windowToken, 0)
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
            .placeholder(R.drawable.place_holder_dish)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.corner_radius)
                ),
            )
            .into(binding.dishImage)
    }

    private fun saveDishAndRedirect(nameAvailable: Boolean) {
        if (nameAvailable) {
            if (dishName.isBlank()) {
                viewModel.createNewDish()
                findNavController().popBackStack(
                    R.id.dishesFragment,
                    false
                )
            } else {
                alertDialog(SAVE)
            }
        } else {
            showToast(requireContext().getString(R.string.already_exists_dish))
        }
    }

    private fun saveNameAndDescriptionInViewModel() {
        viewModel.editNameText(binding.nameDishEt.text.toString())
        viewModel.editDescriptionText(binding.descriptionDishEt.text.toString())
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {
        return if (inFavorite == null || !inFavorite)
            getDrawable(requireContext(), R.drawable.ic_inactive_favorite)
        else
            getDrawable(requireContext(), R.drawable.ic_active_favorite)
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

            DELETE_DISH -> {
                title = requireContext().getString(R.string.title_delete)
                message = requireContext().getString(R.string.message_delete_dish)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }

            DELETE_IMG -> {
                title = requireContext().getString(R.string.title_delete)
                message = requireContext().getString(R.string.message_delete_img_dish)
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
                    viewModel.changeDish()
                    findNavController().navigate(
                        R.id.action_editeCreateDishFragment_to_detailDishFragment,
                        DetailDishFragment.createArgs(binding.nameDishEt.text.toString()),
                    )
                }

                BACK -> {
                    findNavController().popBackStack()
                }

                DELETE_DISH -> {
                    viewModel.deleteDish()
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }

                DELETE_IMG -> {
                    viewModel.deleteDishImg()
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
        private const val AVAILABLE_DISH = "new_dish"
        private const val DELETE_DISH = "delete_dish"
        private const val DELETE_IMG = "delete_img"
        private const val BACK = "back"
        private const val SAVE = "save"

        fun createArgs(dishName: String?): Bundle =
            bundleOf(AVAILABLE_DISH to dishName)
    }
}