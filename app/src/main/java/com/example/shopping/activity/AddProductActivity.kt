package com.example.shopping.activity

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shopping.Model.ItemsModel
import com.example.shopping.ViewModel.MainViewModel
import com.example.shopping.databinding.ActivityAddProductBinding
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private val viewModel: MainViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                binding.ivProductImage.setImageURI(selectedImageUri)
            }
        }

        binding.btnPickImage.setOnClickListener {
            pickImage()
        }

        binding.btnSubmit.setOnClickListener {
            uploadProduct()
        }

        binding.cancelBtn.setOnClickListener {
            navigateBackToMainPage()
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun uploadProduct() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
        val rating = binding.etRating.text.toString().toDoubleOrNull() ?: 0.0
        val currentDateTime = Date()

        // Format date and time separately
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val currentDate = dateFormat.format(currentDateTime)
        val currentTime = timeFormat.format(currentDateTime)

        if (title.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Title and Image are required", Toast.LENGTH_SHORT).show()
            return
        }

        val imageRef = storageReference.child("products/${UUID.randomUUID()}.jpg")
        selectedImageUri?.let { uri ->
            imageRef.putFile(uri).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val newItem = ItemsModel(
                        title = title,
                        description = description,
                        picUrl = listOf(downloadUrl.toString()),
                        price = price,
                        uploadDate = currentDate, // Use the extracted date
                        uploadTime = currentTime, // Use the extracted time
                        rating = rating,
                        numberInCart = 0
                    )
                    viewModel.addNewItem(newItem)
                    Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun navigateBackToMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
