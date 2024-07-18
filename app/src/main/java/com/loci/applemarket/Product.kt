package com.loci.applemarket

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val imageSrc: Int,
    val productName: String,
    val productIntroduce: String,
    val seller: String,
    val price: Int,
    val sellerAddress: String,
    var likeCount: Int,
    var commentCount: Int,
    var isLike: Boolean
) : Parcelable
