package com.example.tfs2.util

import android.app.Application
import com.example.tfs2.model.ItemDatabase
import com.example.tfs2.model.ItemRepository

class ProductApplication: Application() {
    private val database by lazy { ItemDatabase.getDatabase(this) }
    val repository by lazy { ItemRepository(database.itemDao()) }

    override fun onCreate() {
        super.onCreate()
        val scheduler = NotificationScheduler(this)
        scheduler.scheduleDailyExpirationCheck()
    }
}