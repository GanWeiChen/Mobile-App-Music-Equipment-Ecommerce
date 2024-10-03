package com.example.shopping.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1762.Helper.ChangeNumberItemsListener
import com.example.shopping.Adapter.CartAdapter
import com.example.shopping.Database.ManagementCart
import com.example.shopping.Model.OrderModel
import com.example.shopping.databinding.ActivityCartBinding

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagementCart
    private var tax: Double = 0.0

    private var dateSelected = false
    private var timeSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)

        setVariable()
        initDateAndTimePickers()
        initCartList()
        calculateCart()
    }

    private fun checkout() {
        val items = managementCart.getListCart()
        val selectedDate = binding.scheduleDateTxt.text.toString()
        val selectedTime = binding.scheduleTimeTxt.text.toString()

        // Call checkout with the required parameters
        managementCart.checkout(selectedDate, selectedTime)

        val orderItems = items.map { item ->
            OrderModel(
                title = item.title,
                quantity = item.numberInCart,
                price = item.price * item.numberInCart,
                scheduledDate = selectedDate,
                scheduledTime = selectedTime,
                productImage = item.picUrl.firstOrNull() ?: ""
            )
        }

        // Debugging logs
        Log.d("CartActivity", "Order items to be passed: $orderItems")

        val intent = Intent(this, OrderActivity::class.java)
        intent.putParcelableArrayListExtra("ORDER_ITEMS", ArrayList(orderItems))
        startActivity(intent)
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
        binding.checkBtn.setOnClickListener { checkout() }
    }

    private fun initCartList() {
        binding.viewCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.viewCart.adapter = CartAdapter(managementCart.getListCart(), this, object : ChangeNumberItemsListener {
            override fun onChanged() {
                calculateCart()
            }
        })

        with(binding) {
            emptytxt.visibility = if (managementCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility = if (managementCart.getListCart().isEmpty()) View.GONE else View.VISIBLE
        }
    }


    private fun calculateCart() {
        val percentTax = 0.08
        val delivery = 5.0
        tax = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0
        val total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100
        val itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100

        with(binding) {
            totalFee.text = "$$itemTotal"
            taxFee.text = "$$tax"
            deliFee.text = "$$delivery"
            totalItemFee.text = "$$total"
        }
    }

    private fun initDateAndTimePickers() {
        binding.selectDateBtn.setOnClickListener { showDatePicker() }
        binding.selectTimeBtn.setOnClickListener { showTimePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
            binding.scheduleDateTxt.text = date
            dateSelected = true
            enableCheckOutButtonIfReady()
        }, year, month, day)

        datePicker.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.scheduleTimeTxt.text = time
            timeSelected = true
            enableCheckOutButtonIfReady()
        }, hour, minute, true)

        timePicker.show()
    }

    private fun enableCheckOutButtonIfReady() {
        binding.checkBtn.isEnabled = dateSelected && timeSelected
    }
}
