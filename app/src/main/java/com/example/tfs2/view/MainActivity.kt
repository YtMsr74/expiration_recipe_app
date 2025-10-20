package com.example.tfs2.view

import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tfs2.util.ExpirationCheckWorker
import com.example.tfs2.R
import com.example.tfs2.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @ExperimentalGetImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                //R.id.menu_catalog -> replaceFragment(CatalogFragment())
                R.id.menu_products -> replaceFragment(ProductsFragment())
                R.id.menu_recipes -> replaceFragment(RecipesFragment())
                else -> false
            }
        }

        //testNotificationNow()
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment)
            .commit()
        return true
    }

    fun testNotificationNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
                return
            }
        }

        val workRequest = OneTimeWorkRequestBuilder<ExpirationCheckWorker>()
            .setInitialDelay(1, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
}