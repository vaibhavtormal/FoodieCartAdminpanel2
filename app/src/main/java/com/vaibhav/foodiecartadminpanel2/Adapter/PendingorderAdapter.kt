package com.vaibhav.foodiecartadminpanel2.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vaibhav.foodiecartadminpanel2.databinding.PendingorderItemBinding

class PendingorderAdapter(
    private val context: Context,
    private val costomerNames: MutableList<String>,
    private val Quantity: MutableList<String>,
    private val foodImages: MutableList<String>,
    private val itemClicked: OnItemClicked,
) : RecyclerView.Adapter<PendingorderAdapter.PendingOrderViewHolder>() {

    interface OnItemClicked {
        fun onItemClickListenr(position: Int)
        fun onItemAcceptListenr(position: Int)
        fun onItemDispatchListenr(position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            PendingorderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = costomerNames.size
    inner class PendingOrderViewHolder(private val binding: PendingorderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                CousterName.text = costomerNames[position]
                var uriString = foodImages[position]
                PendingOrderQuantity.text = Quantity[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(orderedfoodImageView)
                OrderAcceptButton.apply {
                    if (!isAccepted) {
                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is accepted")
                            itemClicked.onItemAcceptListenr(position)
                        } else {
                            costomerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatch")
                            itemClicked.onItemDispatchListenr(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListenr(position)
                }
            }

        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }
}