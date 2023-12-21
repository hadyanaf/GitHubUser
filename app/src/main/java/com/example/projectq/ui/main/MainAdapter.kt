package com.example.projectq.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.projectq.databinding.ItemUserBinding
import com.example.projectq.domain.model.UserHomeDomainModel

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private val listUser: MutableList<UserHomeDomainModel> = mutableListOf()
    private var onItemClickCallback: OnItemClickCallback? = null

    // ViewHolder class
    inner class MainViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserHomeDomainModel) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgUser)

                tvUsername.text = user.username

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user.username)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding =
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val product = listUser[position]
        holder.bind(product)
    }

    fun updateList(newList: List<UserHomeDomainModel>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(listUser, newList))
        listUser.clear()
        listUser.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    // DiffUtil callback class
    private class UserDiffCallback(
        private val oldList: List<UserHomeDomainModel>,
        private val newList: List<UserHomeDomainModel>
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
        fun onItemClicked(username: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}