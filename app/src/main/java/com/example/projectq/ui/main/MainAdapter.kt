package com.example.projectq.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.projectq.R
import com.example.projectq.data.local.model.ProductDomain
import com.example.projectq.databinding.ItemProductBinding

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private val listProduct: MutableList<ProductDomain> = mutableListOf()
    private var onItemClickCallback: OnItemClickCallback? = null

    // ViewHolder class
    inner class MainViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductDomain) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(product.thumbnail)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgProductThumbnail)

                tvTitleProduct.text = product.title
                tvPrice.text = itemView.context.getString(R.string.txt_product_price, product.price)

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(product.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding =
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int = listProduct.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val product = listProduct[position]
        holder.bind(product)
    }

    fun updateList(newList: List<ProductDomain>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(listProduct, newList))
        listProduct.clear()
        listProduct.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    // DiffUtil callback class
    private class UserDiffCallback(
        private val oldList: List<ProductDomain>,
        private val newList: List<ProductDomain>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}