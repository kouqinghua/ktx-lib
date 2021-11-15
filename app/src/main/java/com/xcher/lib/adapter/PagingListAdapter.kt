package com.xcher.lib.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class PagingListAdapter<T : Any, B : ViewDataBinding>(private val layoutId: Int) :
    PagingDataAdapter<T, PagingListAdapter.ViewHolder<B>>(object :
        DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(
            oldItem: T,
            newItem: T,
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: T,
            newItem: T,
        ): Boolean {
            return oldItem == newItem
        }
    }) {
    class ViewHolder<B>(itemView: View, val binding: B) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        getItem(position)?.let { invoke(holder.binding, it, position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
        val binding: B = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return ViewHolder(binding.root, binding)
    }

    abstract fun invoke(binding: B, data: T, position: Int)

}