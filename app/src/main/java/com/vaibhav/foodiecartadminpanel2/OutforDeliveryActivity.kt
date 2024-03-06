package com.vaibhav.foodiecartadminpanel2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vaibhav.foodiecartadminpanel2.Adapter.DeliveryAdapter
import com.vaibhav.foodiecartadminpanel2.databinding.ActivityOutforDeliveryBinding
import com.vaibhav.foodiecartadminpanel2.model.OrderDetails

class OutforDeliveryActivity : AppCompatActivity() {
    private val binding:ActivityOutforDeliveryBinding by lazy {
        ActivityOutforDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderList:ArrayList<OrderDetails> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.BackButton.setOnClickListener{
            finish()
        }
        //retrieve and display completed order
        retrieveCompleteOrderDetails()
    }

    private fun retrieveCompleteOrderDetails() {
        //initialize Firebase database
        database = FirebaseDatabase.getInstance()

        val completeOrderDetails = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear the list before populating it with new data
                listOfCompleteOrderList.clear()
                for (orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }
                //reverse the list to display order first
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

    private fun setDataIntoRecyclerView() {
        //initialize lists to hold customers' names and payment status
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }
            order.paymentReceived?.let {
                moneyStatus.add(it)
            }
        }

        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.DeliveryRecyclerView.adapter = adapter
        binding.DeliveryRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}




