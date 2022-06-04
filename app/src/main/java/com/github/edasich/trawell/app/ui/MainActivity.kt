package com.github.edasich.trawell.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.edasich.trawell.R
import com.github.edasich.trawell.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var layout: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        layout.fragmentContainerView.getFragment<NavHostFragment>()
            .also {
                layout.bottomNavigationView.setupWithNavController(navController = it.navController)
            }
    }

}