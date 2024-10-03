package com.example.shopping.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopping.Adapter.OrderAdapter
import com.example.shopping.databinding.ActivityOrderBinding
import com.example.shopping.Database.TinyDB
import com.example.shopping.Model.OrderModel

class OrderActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var tinyDB: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tinyDB = TinyDB(this)  // Initialize TinyDB

        // Fetch orders from TinyDB
        fetchOrders()

        binding.okBtn.setOnClickListener {
            // Navigate back to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        binding.clearBtn.setOnClickListener {
            clearOrders()
        }
    }

    private fun fetchOrders() {
        // Retrieve the order items from TinyDB
        val orderItems: ArrayList<OrderModel> = tinyDB.getListOrder("OrderList") ?: arrayListOf()

        if (orderItems.isNullOrEmpty()) {
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "${orderItems.size} items found", Toast.LENGTH_SHORT).show()

            // Set up the RecyclerView
            orderAdapter = OrderAdapter(orderItems)
            binding.recyclerView.adapter = orderAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
    private fun clearOrders() {
        // Clear the order list from TinyDB
        tinyDB.clearOrderList("OrderList")
        Toast.makeText(this, "Orders cleared", Toast.LENGTH_SHORT).show()

        // Refresh the view
        fetchOrders()
    }
}
