package com.loci.applemarket

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loci.applemarket.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var productData: Product
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

        binding.run {

            ivDetailBackButton.setOnClickListener {
                finish()
            }

            ivDetailProductImage.setImageResource(productData.imageSrc)
            tvUserProfileName.text = productData.seller
            tvUserProfileAddress.text = productData.sellerAddress
            tvDetailProductTitle.text = productData.productName
            tvDetailProductContents.text = productData.productIntroduce
            tvDetailProductPrice.text = getString(R.string.comma_number, productData.price)

        }


    }


}