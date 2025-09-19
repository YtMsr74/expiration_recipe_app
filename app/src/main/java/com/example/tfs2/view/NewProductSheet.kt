package com.example.tfs2.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.fragment.app.activityViewModels
import com.example.tfs2.ProductApplication
import com.example.tfs2.databinding.FragmentNewProductBinding
import com.example.tfs2.model.Item
import com.example.tfs2.viewmodel.ItemModelFactory
import com.example.tfs2.viewmodel.ItemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.mlkit.vision.barcode.BarcodeScanner
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@ExperimentalGetImage
class NewProductSheet(var item: Item?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewProductBinding
    private val scanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val code = result.data?.getStringExtra("SCAN_RESULT")
                code?.let { handleScanResult(it) }
            }
        }
    }
    private var date: LocalDate = LocalDate.now()
    private val itemViewModel: ItemViewModel by activityViewModels(
        factoryProducer = {
            val activity = requireActivity()
            val app = activity.application as ProductApplication
            ItemModelFactory(app.repository)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (item != null) {
            binding.title.text = "Изменение продукта"
            binding.buttonAdd.text = "Изменить"
            binding.textName.setText(item!!.name)
        }
        updateDateButtonText()

        binding.buttonDate.setOnClickListener {
            openDatePicker()
        }
        binding.buttonAdd.setOnClickListener {
            saveAction()
        }
        binding.buttonScan.setOnClickListener {
            startScanner()
        }
    }

    private fun formatDate(date: LocalDate): String {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(date)
    }

    private fun updateDateButtonText() {
        binding.buttonDate.text = formatDate(date)
    }

    private fun openDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
            updateDateButtonText()
        }
        val dialog = DatePickerDialog(requireActivity(), listener, date.year, date.monthValue - 1, date.dayOfMonth)
        dialog.setTitle("Срок годности")
        dialog.show()
    }

    private fun startScanner() {
        val intent = Intent(requireContext(), ScanActivity::class.java)
        scanLauncher.launch(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun handleScanResult(code: String) {
        binding.textName.setText("Продукт #$code")
    }

    private fun saveAction() {
        val name = binding.textName.text.toString()
        if (name.isEmpty()) {
            binding.textName.error = "Введите название продукта"
            binding.textName.requestFocus()
            return
        }
        if (date.isBefore(LocalDate.now())) {
            binding.buttonDate.error = "Продукт уже просрочен"
            Toast.makeText(context, "Продукт уже просрочен", Toast.LENGTH_SHORT).show()
            return
        }
        if (item == null) {
            val newItem = Item(name = name, expiryDate = date)
            itemViewModel.addItem(newItem)
        }
        else {
            item!!.name = name
            item!!.expiryDate = date
            itemViewModel.updateItem(item!!)
        }
        dismiss()
    }
}