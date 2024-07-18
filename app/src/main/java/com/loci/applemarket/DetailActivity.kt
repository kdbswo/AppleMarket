package com.loci.applemarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loci.applemarket.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var productData: Product
    private var productPosition: Int = -1
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        productData = intent.getParcelableExtra("product")!!
        productPosition = intent.getIntExtra("position", -1)
        binding.run {

            ivDetailBackButton.setOnClickListener {
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                intent.putExtra("product", productData)
                intent.putExtra("position", productPosition)
                setResult(RESULT_OK, intent)
                finish()
            }

            ivDetailProductImage.setImageResource(productData.imageSrc)
            tvUserProfileName.text = productData.seller
            tvUserProfileAddress.text = productData.sellerAddress
            tvDetailProductTitle.text = productData.productName
            tvDetailProductContents.text = productData.productIntroduce
            tvDetailProductPrice.text = getString(R.string.comma_number, productData.price)

            ivIsLikeButton.setOnClickListener {
                isLikeToggle()
                setIsLikeResource()
            }

            setIsLikeResource()
        }


    }

    private fun isLikeToggle() {
        if (productData.isLike) {
            ProductData.productList[productPosition].likeCount -= 1
        } else {
            ProductData.productList[productPosition].likeCount += 1
        }
        productData.isLike = !productData.isLike
        ProductData.productList[productPosition].isLike = productData.isLike
    }

    private fun setIsLikeResource() {
        if (productData.isLike) {
            binding.ivIsLikeButton.setImageResource(R.drawable.red_heart_shape)
        } else {
            binding.ivIsLikeButton.setImageResource(R.drawable.heart_shape)
        }
    }


}