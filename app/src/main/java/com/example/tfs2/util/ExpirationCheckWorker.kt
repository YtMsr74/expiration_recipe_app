package com.example.tfs2.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tfs2.model.ItemDatabase
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.tfs2.R
import com.example.tfs2.model.Item
import com.example.tfs2.view.MainActivity
import java.time.LocalDate

class ExpirationCheckWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            checkExpiringProducts()
            Result.success()
        }
        catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun checkExpiringProducts() {
        val database = ItemDatabase.getDatabase(applicationContext)
        val itemDao = database.itemDao()

        val today = LocalDate.now()
        val warningDate = today.plusDays(2)

        val expiringProducts = itemDao.getExpiringProducts(today, warningDate)
        if (expiringProducts.isNotEmpty()) {
            showNotification(expiringProducts)
        }
    }

    private fun showNotification(products: List<Item>) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        val message = if (products.size == 1) {
            "Срок годности ${products[0].name} истекает через 2 дня!"
        }
        else {
            "Срок годности ${products.size} продуктов истекает через 2 дня!"
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(applicationContext, "expiration_channel")
            .setSmallIcon(R.drawable.baseline_access_time_24)
            .setContentTitle("Скоро истекает срок годности")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel("expiration_channel", "Уведомление о сроке годности", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Уведомление о скором истечении срока годности продуктов"
        }
        notificationManager.createNotificationChannel(channel)
    }
}