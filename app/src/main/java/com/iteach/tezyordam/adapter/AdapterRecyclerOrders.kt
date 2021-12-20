package com.iteach.tezyordam.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.iteach.tezyordam.base.Order
import com.iteach.tezyordam.databinding.ItemHomeBinding


class AdapterRecyclerOrders(var orderclick: OnItemClickListner,var items:ArrayList<Order>,val context: Context) : RecyclerView.Adapter<AdapterRecyclerOrders.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemHomeBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding =ItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding) }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            binding.order.setOnClickListener {
                orderclick.orderClicked(position)
            }


            if (position+3%20==0){
                //Toast.makeText(context,"19",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setData(list: ArrayList<Order>){
        items.clear()
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListner{
        fun orderClicked(order: Int)
    }
}