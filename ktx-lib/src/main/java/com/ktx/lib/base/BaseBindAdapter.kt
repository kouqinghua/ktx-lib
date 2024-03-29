package com.ktx.lib.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBindAdapter<T, B : ViewDataBinding>(private val layoutId: Int) :
    RecyclerView.Adapter<BaseBindAdapter.ViewHolder<B>>() {


    private var lists = arrayListOf<T>()
    private var type: String? = null

    private var onItemClickListener: OnItemClickListener? = null
    private var onTypeItemClickListener: OnTypeItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setOnTypeItemClickListener(
        onTypeItemClickListener: OnTypeItemClickListener,
        type: String = "default",
    ) {
        this.onTypeItemClickListener = onTypeItemClickListener
        this.type = type
    }

    class ViewHolder<B>(itemView: View, val binding: B) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
        val binding: B = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        invoke(holder.binding, lists[position], position)

        onItemClickListener?.let {
            holder.binding.root.setOnClickListener {
                onItemClickListener?.onItemClick(position)
            }
        }

        onTypeItemClickListener?.let {
            holder.binding.root.setOnClickListener {
                onTypeItemClickListener?.onTypeItemClick(position, type!!)
            }
        }
    }

    abstract fun invoke(binding: B, data: T, position: Int)

    override fun getItemCount(): Int {
        return lists.size
    }

    fun setData(it: List<T>) {
        if (it.isNotEmpty()) {
            lists.clear()
            lists.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun setMore(more: List<T>?) {
        more?.let {
            val p: Int = lists.size
            lists.addAll(it)
            notifyItemRangeChanged(p, lists.size)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnTypeItemClickListener {
        fun onTypeItemClick(position: Int, type: String)
    }
}