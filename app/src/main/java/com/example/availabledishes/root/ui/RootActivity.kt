package com.example.availabledishes.root.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.example.availabledishes.R
import com.example.availabledishes.databinding.ActivityRootBinding
import com.example.availabledishes.my_products.ui.fragment.ProductsFragment

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
//        val navController = navHostFragment.navController

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                this.add(R.id.rootFragmentContainerView, ProductsFragment())
            }
        }


    }
}