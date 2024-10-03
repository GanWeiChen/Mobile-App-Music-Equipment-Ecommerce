// MainActivity.kt
package com.example.shopping.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.shopping.Model.SliderModel
import com.example.shopping.Adapter.ProductAdapter
import com.example.shopping.Adapter.SliderAdapter
import com.example.shopping.Database.UserAuth
import com.example.shopping.ViewModel.MainViewModel
import com.example.shopping.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAuth: UserAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = MainViewModel()
        userAuth = UserAuth(this)

        val currentUser = userAuth.getCurrentUser()
        if (currentUser != null) {
            val userId = currentUser.uid
            val database = FirebaseDatabase.getInstance().reference

            database.child("users").child(userId).get().addOnSuccessListener { dataSnapshot ->
                val userName = dataSnapshot.child("name").value.toString()
                binding.username.text = userName
            }.addOnFailureListener {
                Log.e("MainActivity", "Error getting user name", it)
            }
        }

        binding.btnAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
        binding.orderBtn.setOnClickListener {
            // Optionally pass data or handle navigation
            startActivity(Intent(this, OrderActivity::class.java))
        }

        initBanner()
        initProduct()
        initBottomMenu()
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener{ startActivity((Intent(this@MainActivity,CartActivity::class.java)))}
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items ->
            Log.d("MainActivity", "Banners fetched: $items")
            if (items != null && items.isNotEmpty()) {
                banners(items)
                binding.progressBarBanner.visibility = View.GONE
            }
        })
        viewModel.loadBanners()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewpagerSlider.adapter = SliderAdapter(images, binding.viewpagerSlider)
        binding.viewpagerSlider.clipToPadding = false
        binding.viewpagerSlider.clipChildren = false
        binding.viewpagerSlider.offscreenPageLimit = 3
        binding.viewpagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer)

        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpagerSlider)
        } else {
            binding.dotIndicator.visibility = View.GONE
        }
    }



    private fun initProduct(){
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.product.observe(this, Observer{
            binding.viewProduct.layoutManager = GridLayoutManager(this@MainActivity,2)
            binding.viewProduct.adapter = ProductAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadProduct()
    }
}
