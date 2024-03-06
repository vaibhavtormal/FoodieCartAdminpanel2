package com.vaibhav.foodiecartadminpanel2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.vaibhav.foodiecartadminpanel2.Adapter.OrderDetailsAdapter
import com.vaibhav.foodiecartadminpanel2.databinding.ActivityOrderDetailsBinding
import com.vaibhav.foodiecartadminpanel2.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodNames: ArrayList<String> = arrayListOf()
    private var foodImages:  ArrayList<String> = arrayListOf()
    private var foodQuantity:  ArrayList<Int> = arrayListOf()
    private var foodPrice: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.BackButton.setOnClickListener {
            finish()
        }


        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val recivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        recivedOrderDetails.let { orderDetails ->
            foodNames = recivedOrderDetails.foodNames  as ArrayList<String>
            foodImages = recivedOrderDetails.foodImages as ArrayList<String>
            foodQuantity = recivedOrderDetails.foodQuantities as ArrayList<Int>
            foodPrice = recivedOrderDetails.foodPrices as ArrayList<String>
            userName = recivedOrderDetails.userName
            address = recivedOrderDetails.address
            phoneNumber =recivedOrderDetails.phoneNumber
            totalPrice = recivedOrderDetails.totalPrice

            setUserDetails()
            setAdapter()
        }

        }


    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this,foodNames,foodImages,foodQuantity,foodPrice)
        binding.orderDetailsRecyclerView.adapter = adapter
    }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = address

        binding.totalPay.text = totalPrice
        binding.phoneNumber.text = phoneNumber

    }
}