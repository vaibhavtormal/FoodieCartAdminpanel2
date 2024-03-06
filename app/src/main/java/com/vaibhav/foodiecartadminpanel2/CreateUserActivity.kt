package com.vaibhav.foodiecartadminpanel2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vaibhav.foodiecartadminpanel2.databinding.ActivityCreateUserBinding

class CreateUserActivity : AppCompatActivity() {
    private val binding :ActivityCreateUserBinding by lazy {
        ActivityCreateUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.BackButton.setOnClickListener{
            finish()
        }
    }
}