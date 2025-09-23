package com.example.tfs2.viewmodel

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfs2.model.ItemCodeResponse
import com.example.tfs2.model.ItemRequestManager
import com.example.tfs2.view.listener.ItemInfoListener
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ScanViewModel : ViewModel() {
    val scanResult = MutableStateFlow<String?>(null)
    val error = MutableStateFlow<String?>(null)
    private var isProcessingImage = false
    private var lastError = 0L
    private val errorDelay = 4000L

    private val scanner: BarcodeScanner = BarcodeScanning.getClient()
    private val itemRequestManager = ItemRequestManager()

    @ExperimentalGetImage
    fun processImage(image: ImageProxy) {
        if (isProcessingImage) {
            image.close()
            return
        }
        isProcessingImage = true

        val mediaImage = image.image
        if (mediaImage == null) {
            image.close()
            isProcessingImage = false
            return
        }

        try {
            val visionImage = InputImage.fromMediaImage(
                mediaImage,
                image.imageInfo.rotationDegrees
            )
            scanner.process(visionImage)
                .addOnSuccessListener { codes ->
                    viewModelScope.launch {
                        val codeValue = codes.firstOrNull()?.rawValue
                        if (codeValue != null) {
                            getProductName(codeValue)
                            error.value = null
                        }
                        isProcessingImage = false
                    }
                }
                .addOnFailureListener { e ->
                    viewModelScope.launch {
                        showErrorWithDelay("Ошибка сканирования: ${e.message}")
                        isProcessingImage = false
                    }
                }
                .addOnCompleteListener {
                    image.close()
                }
        } catch (e: Exception) {
            image.close()
            isProcessingImage = false
            viewModelScope.launch {
                showErrorWithDelay("Ошибка обработки: ${e.message}")
            }
        }
    }

    fun resetScanResult() {
        scanResult.value = null
        error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        scanner.close()
    }

    private fun getProductName(code: String) {
        itemRequestManager.getItemByCode(object: ItemInfoListener {
            override fun didFetch(response: ItemCodeResponse?, message: String) {
                viewModelScope.launch {
                    isProcessingImage = false
                    when {
                        response?.status == 1 -> {
                            val name = response.product?.product_name ?: response.product?.product_nameEn
                            if (name != null) {
                                scanResult.value = name
                                error.value = null
                            } else {
                                scanResult.value = null
                                showErrorWithDelay("Название не найдено")
                            }
                        }
                        else -> {
                            scanResult.value = null
                            showErrorWithDelay("Продукт не найден: ${response?.statusVerbose ?: "Неизвестная ошибка"}")
                        }
                    }
                }
            }

            override fun didError(message: String) {
                viewModelScope.launch {
                    isProcessingImage = false
                    scanResult.value = null
                    showErrorWithDelay("Ошибка загрузки: $message")
                }
            }
        }, code)
    }

    private fun showErrorWithDelay(message: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastError > errorDelay) {
            error.value = message
            lastError = currentTime
        }
    }
}