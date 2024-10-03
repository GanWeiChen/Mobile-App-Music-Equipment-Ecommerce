package com.example.shopping.Model

import android.os.Parcel
import android.os.Parcelable

data class OrderModel(
    val title: String?,
    val quantity: Int,
    val price: Double,
    val scheduledDate: String?,
    val scheduledTime: String?,
    val productImage: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    // New constructor that accepts an ItemsModel
    constructor(itemsModel: ItemsModel) : this(
        title = itemsModel.title,
        quantity = itemsModel.numberInCart,
        price = itemsModel.price,
        scheduledDate = itemsModel.uploadDate,
        scheduledTime = itemsModel.uploadTime,
        productImage = itemsModel.picUrl.toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(quantity)
        parcel.writeDouble(price)
        parcel.writeString(scheduledDate)
        parcel.writeString(scheduledTime)
        parcel.writeString(productImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel {
            return OrderModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderModel?> {
            return arrayOfNulls(size)
        }
    }
}
