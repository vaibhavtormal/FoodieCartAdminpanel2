package com.vaibhav.foodiecartadminpanel2

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.vaibhav.foodiecartadminpanel2.databinding.ActivityAdditemBinding
import com.vaibhav.foodiecartadminpanel2.model.AllMenu

class AdditemActivity : AppCompatActivity() {
    // Food item Details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAdditemBinding by lazy {
        ActivityAdditemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.addItemButton.setOnClickListener {
            // Get data from Field
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredients.text.toString().trim()

            if (foodName.isNotBlank() && foodPrice.isNotBlank() && foodDescription.isNotBlank() && foodIngredient.isNotBlank() && foodImageUri != null) {
                uploadData()
            } else {
                Toast.makeText(this, "Fill All the Details and select an Image", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.BackButton.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
        // Get a reference to the "Menu" node in the database
        val menuRef: DatabaseReference = database.getReference("menu")
        // Generate a unique Key for the Menu item
        val newItemKey: String = menuRef.push().key ?: ""

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("menu_images/$newItemKey.jpg")
        val uploadTask = imageRef.putFile(foodImageUri!!)

        uploadTask.addOnSuccessListener { _ ->
            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                // Create a new menu item
                val newItem = AllMenu(
                    newItemKey,
                    foodName,
                    foodPrice,
                    foodDescription,
                    foodIngredient,
                    downloadUrl.toString()
                )

                newItemKey.let { key ->
                    menuRef.child(key).setValue(newItem).addOnSuccessListener {
                        Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.selectedImage.setImageURI(it)
                foodImageUri = it
            }
        }
}
