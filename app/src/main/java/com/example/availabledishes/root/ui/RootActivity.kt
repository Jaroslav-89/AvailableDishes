package com.example.availabledishes.root.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.availabledishes.R
import com.example.availabledishes.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
//    private var backPressedTime: Long = 0
//    private lateinit var backToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (backPressedTime + 2000 > System.currentTimeMillis()) {
//                    backToast.cancel()
//                    finishAffinity()
//                    return
//                } else {
//                    backToast = Toast.makeText(
//                        this@RootActivity,
//                        R.string.exit_alert,
//                        Toast.LENGTH_SHORT
//                    )
//                    backToast.show()
//                }
//                backPressedTime = System.currentTimeMillis()
//            }
//        })
    }
}