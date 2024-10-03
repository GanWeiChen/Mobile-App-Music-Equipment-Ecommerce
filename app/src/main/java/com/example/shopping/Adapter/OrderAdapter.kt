package com.example.shopping.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopping.Model.OrderModel
import com.example.shopping.databinding.ItemOrderBinding

class OrderAdapter(private val orderList: ArrayList<OrderModel>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderList[position]
        holder.bind(orderItem)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderModel) {
            with(binding) {
                orderTitle.text = orderItem.title
                orderQuantity.text = "Quantity: ${orderItem.quantity}"
                orderPrice.text = "Price: $${orderItem.price}"
                orderDate.text = "Pay Date: ${orderItem.scheduledDate}"
                orderTime.text = "Pay Time: ${orderItem.scheduledTime}"
                // Load image using your preferred image loading library
                // productImage.setImageResource(orderItem.productImage)
                Glide.with(binding.root.context)
                    .load(orderItem.productImage)
                    .into(productImage)
            }
        }
    }
}
