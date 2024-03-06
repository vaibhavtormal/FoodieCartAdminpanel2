package com.vaibhav.foodiecartadminpanel2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vaibhav.foodiecartadminpanel2.databinding.ActivitySignUpBinding
import com.vaibhav.foodiecartadminpanel2.model.UserModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfrestaurant: String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //intialize firebase Auth
        auth = Firebase.auth
        //intialize Firebase database
        database = Firebase.database.reference

        binding.createUserButton.setOnClickListener {
            //get text from edittext like a email password
            userName = binding.nameOwner.text.toString()
            nameOfrestaurant = binding.restaurantName.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (userName.isBlank()||nameOfrestaurant.isBlank()||email.isBlank()||password.isBlank()){
                Toast.makeText(this,"Please fill all details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }
        binding.alreadyHaveAccountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val locationList = arrayOf("Mumbai","Pune", "Mehkar", "Chikhali", "Buldhana","Nashik","Jalgaon","Indore","Chatrpati Sambhajinagar","Shegaon")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val atuoCompleteTextView = binding.listofLocation
        atuoCompleteTextView.setAdapter(adapter)

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                finish()
            }
            else{
                Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure",task.exception)
            }
        }
    }
    //save data into server
    private fun saveUserData() {
        //get text from edittext like a email password
        userName = binding.nameOwner.text.toString().trim()
        nameOfrestaurant = binding.restaurantName.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user =UserModel(userName,nameOfrestaurant,email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //save user data Firebase database
        database.child("user").child(userId).setValue(user)
    }
}