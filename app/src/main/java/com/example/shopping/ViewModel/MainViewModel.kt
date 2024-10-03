package com.example.shopping.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.Model.ItemsModel
import com.example.shopping.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel: ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance("https://shopping-2807b-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _product = MutableLiveData<MutableList<ItemsModel>>()

    val product: LiveData<MutableList<ItemsModel>> = _product
    val banners: LiveData<List<SliderModel>> = _banner

    fun loadBanners() {
        val ref = firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }



    fun loadProduct() {
        val ref = firebaseDatabase.getReference("Items")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _product.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    fun addNewItem(item: ItemsModel) {
        val ref = firebaseDatabase.getReference("Items")
        ref.push().setValue(item).addOnSuccessListener {
            loadProduct() // Reload data to get the latest items
        }.addOnFailureListener {
            // Handle failure
        }
    }
}
