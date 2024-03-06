package com.vaibhav.foodiecartadminpanel2.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.vaibhav.foodiecartadminpanel2.databinding.ItemItemBinding
import com.vaibhav.foodiecartadminpanel2.model.AllMenu

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position:Int)->Unit
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {
    private val itemQuantities = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size
    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity:Int = itemQuantities[position]
                val menuItem:AllMenu= menuList[position]
                val uriString:String?= menuItem.foodImage
                val uri = Uri.parse(uriString)
                foodNamtext.text = menuItem.foodName
                priceTextView.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodImageView)
                quantityTextView.text=quantityTextView.toString()
                MinusButton.setOnClickListener {
                    dcreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                DeleteButton.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }
        }

        private fun dcreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.quantityTextView.text=itemQuantities[position].toString()
            }

        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] <10) {
                itemQuantities[position]++
                binding.quantityTextView.text=itemQuantities[position].toString()
            }
        }

        private fun deleteQuantity(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList.size)

        }
    }
}