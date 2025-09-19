package com.example.tfs2.viewmodel

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanViewModel : ViewModel() {
    private val _scanResult = MutableStateFlow<String?>(null)
    val scanResult: StateFlow<String?> = _scanResult
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val scanner: BarcodeScanner = BarcodeScanning.getClient()
    private val _isProcessing = MutableStateFlow(false)
    private var isProcessingImage = false

    @ExperimentalGetImage
    fun processImage(image: ImageProxy) {
        if (isProcessingImage) {
            image.close()
            return
        }
        isProcessingImage = true
        _isProcessing.value = true

        val mediaImage = image.image
        if (mediaImage == null) {
            image.close()
            isProcessingImage = false
            _isProcessing.value = false
            viewModelScope.launch {
                _error.value = "Изображение не доступно"
            }
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
                            _scanResult.value = codeValue
                            _error.value = null
                        }
                        isProcessingImage = false
                        _isProcessing.value = false
                    }
                }
                .addOnFailureListener { e ->
                    viewModelScope.launch {
                        _error.value = "Ошибка сканирования: ${e.message}"
                        isProcessingImage = false
                        _isProcessing.value = false
                    }
                }
                .addOnCompleteListener {
                    image.close()
                }
        } catch (e: Exception) {
            image.close()
            isProcessingImage = false
            _isProcessing.value = false
            viewModelScope.launch {
                _error.value = "Ошибка обработки: ${e.message}"
            }
        }
    }

    fun resetScanResult() {
        _scanResult.value = null
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        scanner.close()
    }
}