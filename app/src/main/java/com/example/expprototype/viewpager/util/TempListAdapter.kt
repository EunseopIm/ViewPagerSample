package com.example.expprototype.viewpager.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expprototype.databinding.ItemTempListBinding


class TempListAdapter(val context: Context?, val list: ArrayList<Int>) : RecyclerView.Adapter<TempListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val binding = ItemTempListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        if (list.size > position) {
            holder.bindView(context, position)
        }
    }

    class CustomViewHolder(private val binding: ItemTempListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(context: Context?, position: Int) {}
    }
}