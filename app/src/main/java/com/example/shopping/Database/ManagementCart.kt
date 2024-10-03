package com.example.shopping.Database

import android.content.Context
import android.widget.Toast
import com.example.project1762.Helper.ChangeNumberItemsListener
import com.example.shopping.Model.ItemsModel
import com.example.shopping.Model.OrderModel

class ManagementCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertFood(item: ItemsModel) {
        var listFood = getListCart()
        val existAlready = listFood.any { it.title == item.title }
        val index = listFood.indexOfFirst { it.title == item.title }

        if (existAlready) {
            listFood[index].numberInCart = item.numberInCart
        } else {
            listFood.add(item)
        }
        tinyDB.putListObject("CartList", listFood)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listFood: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listFood[position].numberInCart == 1) {
            listFood.removeAt(position)
        } else {
            listFood[position].numberInCart--
        }
        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun plusItem(listFood: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listFood[position].numberInCart++
        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

//    fun getTotalFee(): Double {
//        val listFood = getListCart()
//        var fee = 0.0
//        for (item in listFood) {
//            fee += item.price * item.numberInCart
//        }
//        return fee
//    }
    fun getTotalFee(): Double {
        val cartList = getListCart()
        return cartList.sumOf { it.price * it.numberInCart }
}


    fun getOrderList(): ArrayList<OrderModel> {
        return tinyDB.getListOrder("OrderList") ?: arrayListOf()
    }

    fun checkout(selectedDate: String, selectedTime: String) {
        val cartItems = getListCart()
        val orderList = getOrderList() // Retrieve existing orders
        val newOrderList = ArrayList<OrderModel>()

        for (item in cartItems) {
            val order = OrderModel(
                title = item.title,
                quantity = item.numberInCart,
                price = item.price,
                scheduledDate = selectedDate,
                scheduledTime = selectedTime,
                productImage = item.picUrl.firstOrNull() ?: "" // Access the first URL or an empty string
            )
            newOrderList.add(order)
        }

        // Combine existing orders with new orders
        newOrderList.addAll(orderList)

        // Save orders to TinyDB
        tinyDB.putListOrder("OrderList", newOrderList)

        // Clear the cart
        tinyDB.remove("CartList")

        Toast.makeText(context, "Checkout successful!", Toast.LENGTH_SHORT).show()
    }


}
