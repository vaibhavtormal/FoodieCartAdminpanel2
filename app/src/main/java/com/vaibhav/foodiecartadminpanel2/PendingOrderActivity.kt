package com.vaibhav.foodiecartadminpanel2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vaibhav.foodiecartadminpanel2.Adapter.PendingorderAdapter
import com.vaibhav.foodiecartadminpanel2.databinding.ActivityPendingOrderBinding
import com.vaibhav.foodiecartadminpanel2.model.OrderDetails

class PendingOrderActivity : AppCompatActivity(), PendingorderAdapter.OnItemClicked {
    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    /*  override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(binding.root)
          //initialization of database
          database = FirebaseDatabase.getInstance()
          //initialization of databaseReferece
          databaseOrderDetails = database.reference.child("OrderDetails")

          getOrderDetails()
          binding.BackButton.setOnClickListener {
              finish()
          }
      }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialization of database
        database = FirebaseDatabase.getInstance()
        // initialization of databaseReference
        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()
        binding.BackButton.setOnClickListener {
            finish()
        }
    }


    private fun getOrderDetails() {
        //retrieve order details from Firebase database
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)

                    }
                }
                addDataToListForRecylerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecylerView() {
        for (orderItem in listOfOrderItem) {
            //add data to respective list for populating the recylerView
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter =
            PendingorderAdapter(this, listOfName, listOfPrice, listOfImageFirstFoodOrder, this)
        binding.pendingOrderRecyclerView.adapter = adapter
    }

    override fun onItemClickListenr(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptListenr(position: Int) {
        //handle item acceptance and update database
        // val  childItemPushKey = listOfOrderItem[position]//.itempushkey
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickedItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)

        }
        clickedItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }

    override fun onItemDispatchListenr(position: Int) {
        //handle item disaccept and update database
        val  dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderreferance = database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderreferance.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val  orderDetailsItemReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order Is Dispatch", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, "Order Is not  Dispatch", Toast.LENGTH_SHORT).show()
            }

    }


    private fun updateOrderAcceptStatus(position: Int) {
        //update order acceptance in user's by history and order details
        val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference =
            database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory")
                .child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }
}