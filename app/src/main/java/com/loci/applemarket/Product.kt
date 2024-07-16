package com.loci.applemarket

data class Product(
    val id: Int,
    val imageSrc: Int,
    val productName: String,
    val productIntroduce: String,
    val seller: String,
    val price: Int,
    val sellerAddress: String,
    var likeCount: Int,
    var commentCount: Int
)
