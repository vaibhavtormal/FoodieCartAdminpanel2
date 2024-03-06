package com.vaibhav.foodiecartadminpanel2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vaibhav.foodiecartadminpanel2.Adapter.MenuItemAdapter
import com.vaibhav.foodiecartadminpanel2.databinding.ActivityAllitemBinding
import com.vaibhav.foodiecartadminpanel2.model.AllMenu

class AllitemActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var menuItems :ArrayList<AllMenu> = ArrayList()
    private val binding : ActivityAllitemBinding by  lazy {
        ActivityAllitemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()



        binding.BackButton.setOnClickListener{
            finish()
        }


    }

    private fun retrieveMenuItem() {
       database = FirebaseDatabase.getInstance()
        val foodReference:DatabaseReference =database.reference.child("menu")

        //fetch data from database
        foodReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Clear exit data before populating
                menuItems.clear()
                //loop for through each food item
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error:${error.message} ")
            }

        })
    }
    private fun setAdapter(){

        val adapter = MenuItemAdapter(this@AllitemActivity,menuItems,databaseReference){position ->
            deleteMenuItem(position)
        }
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuRecyclerView.adapter=adapter
    }

    private fun deleteMenuItem(position: Int) {
        val menuItemsToDelete = menuItems[position]
        val menuItemKey = menuItemsToDelete.key
        val foodMenuReference = database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.menuRecyclerView.adapter?.notifyItemRemoved(position)
            }
            else{
                Toast.makeText(this, "Item not Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}