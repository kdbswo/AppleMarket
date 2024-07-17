package com.loci.applemarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loci.applemarket.databinding.ItemRecyclerviewBinding

class MyAdapter(val mItems: MutableList<Product>) : RecyclerView.Adapter<MyAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }


        holder.run {
            productImg.setImageResource(mItems[position].imageSrc)
            productName.text = mItems[position].productName
            productAddress.text = mItems[position].sellerAddress

            val context = holder.itemView.context
            productPrice.text = context.getString(R.string.comma_number, mItems[position].price)

            chatCount.text = mItems[position].commentCount.toString()
            likeCount.text = mItems[position].likeCount.toString()
        }
    }

    inner class Holder(binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val productImg = binding.ivItemProduct
        val productName = binding.tvItemProductName
        val productAddress = binding.tvItemProductAddress
        val productPrice = binding.tvItemProductPrice
        val chatCount = binding.tvItemChatCount
        val likeCount = binding.tvItemLikeCount
    }
}









