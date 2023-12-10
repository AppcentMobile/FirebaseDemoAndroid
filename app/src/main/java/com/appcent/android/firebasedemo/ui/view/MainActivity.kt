package com.appcent.android.firebasedemo.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.appcent.android.firebasedemo.R
import com.appcent.android.firebasedemo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var firebaseAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initStartingPage()
    }

    fun hideProgress() {
        binding.progressLayout.isVisible = false
    }

    fun showProgress() {
        binding.progressLayout.isVisible = true
    }

    private fun initStartingPage() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_main)

        if (firebaseAuth.currentUser == null) {
            graph.setStartDestination(R.id.nav_authentication)
        } else {
            graph.setStartDestination(R.id.nav_conversations)
        }
        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }
}