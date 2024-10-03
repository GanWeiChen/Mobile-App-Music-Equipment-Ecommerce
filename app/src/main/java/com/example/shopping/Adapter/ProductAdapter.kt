package com.example.shopping.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.shopping.Model.ItemsModel
import com.example.shopping.R
import com.example.shopping.activity.DetailActivity
import com.example.shopping.databinding.ViewholderRecommendedBinding

class ProductAdapter(private val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var context: Context? = null

    class ViewHolder(val binding: ViewholderRecommendedBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderRecommendedBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < 0 || position >= items.size) {
            Log.e("PopularAdapter", "Index out of bounds: $position")
            return
        }

        val item = items[position]
        holder.binding.titletxt.text = items[position].title
        holder.binding.pricetxt.text = "$" + items[position].price.toString()
        holder.binding.ratingtxt.text = items[position].rating.toString()

        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(item.picUrl.getOrNull(0)) // Use getOrNull to avoid IndexOutOfBoundsException
            .apply(requestOptions)
            .error(R.drawable.ic_launcher_foreground) // Replace with your error image
            .fallback(R.drawable.ic_launcher_foreground) // Replace with your placeholder image
            .into(holder.binding.picProduct)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", items[position])
            holder.itemView.context.startActivity(intent)
        }

    }
}
