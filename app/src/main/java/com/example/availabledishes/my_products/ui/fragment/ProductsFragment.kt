package com.example.availabledishes.my_products.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.availabledishes.databinding.FragmentProductsBinding

class ProductsFragment : Fragment()  {

    private lateinit var binding: FragmentProductsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }
}