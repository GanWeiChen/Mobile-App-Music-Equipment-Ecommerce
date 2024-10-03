package com.example.shopping.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopping.Adapter.ModelAdapter
import com.example.shopping.Database.ManagementCart
import com.example.shopping.Model.ItemsModel
import com.example.shopping.Model.SliderModel
import com.example.shopping.Adapter.SizeAdapter
import com.example.shopping.Adapter.SliderAdapter
import com.example.shopping.databinding.ActivityDetailBinding

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private var numberOrder = 1
    private lateinit var managementCart: ManagementCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)


        getBundle()
        banners()
        initLists()
    }

    private fun initLists() {
        val sizeList = ArrayList<String>()
        for (size in item.size){
            sizeList.add(size.toString())
        }

        binding.sizeList.adapter = SizeAdapter(sizeList)
        binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl){
            colorList.add(imageUrl)
        }

        binding.colorList.adapter = ModelAdapter(colorList)
        binding.colorList.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
    }

    private fun banners(){
        val sliderItems = ArrayList<SliderModel>()
        for (imageUrl in item.picUrl){
            sliderItems.add(SliderModel(imageUrl))
        }

        binding.slider.adapter = SliderAdapter(sliderItems,binding.slider)
        binding.slider.clipToPadding = true
        binding.slider.clipChildren = true
        binding.slider.offscreenPageLimit = 1

        if(sliderItems.size > 1){
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }

    }
    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!

        binding.titletxt.text = item.title
        binding.description.text = item.description
        binding.pricetxt.text = "$" + item.price
        binding.ratingtxt.text = "${item.rating} Rating"
        binding.uploadDateTimeTxt.text = "Uploaded on: ${item.uploadDate} ${item.uploadTime}" // Concatenate date and time
        binding.addToCartBtn.setOnClickListener {
            item.numberInCart = numberOrder
            managementCart.insertFood(item)
        }
        binding.backBtn.setOnClickListener { finish() }
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))
        }
    }
}