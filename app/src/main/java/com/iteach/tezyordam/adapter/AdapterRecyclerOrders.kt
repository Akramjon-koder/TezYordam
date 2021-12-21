package com.iteach.tezyordam.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iteach.tezyordam.R
import com.iteach.tezyordam.base.Order
import com.iteach.tezyordam.databinding.ItemHomeBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterRecyclerOrders(var orderclick: OnItemClickListner,var items:ArrayList<Order?>,val context: Context) : RecyclerView.Adapter<AdapterRecyclerOrders.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemHomeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding =ItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding) }
    private var currentlocation: Location? = null
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            binding.order.setOnClickListener {
                items.get(position)?.let { it1 -> orderclick.orderClicked(it1) }
            }
            binding.complaint.text = items.get(position)?.complaint.toString()
            binding.phone.text = items.get(position)?.phone.toString()
            if (currentlocation!=null){
                Log.e("Location",""+items.get(position)!!.latitude+" "+items.get(position)!!.longitude)
                binding.distance.text = "${distanse(currentlocation!!.latitude,currentlocation!!.longitude,items.get(position)!!.latitude,items.get(position)!!.longitude).toInt()} km"
            }

            if (items.get(position)?.condition==1){
                binding.timerLayout.setBackgroundColor(Color.rgb(244,67,54))
            }else{
                binding.timerLayout.setBackgroundColor(Color.rgb(252,211,77))
            }
            if (position+3%20==0){
                //Toast.makeText(context,"19",Toast.LENGTH_LONG).show()
            }

            val formatter = SimpleDateFormat("hh:mm")
            val calendar = Calendar.getInstance()

            binding.timerText.text = formatter.format(calendar.getTime())

        }
    }

    fun setData(location: Location?, list: ArrayList<Order?>){
        currentlocation = location
        items.clear()
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListner{
        fun orderClicked(order: Order)
    }

    private fun distanse(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                (Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                        * Math.cos(deg2rad(theta)))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 // 60 nautical miles  per degree of seperation  //Seperatsiya darajasiga 60 dengiz mil
        dist = dist * 1852 // 1820 meters per nautial mile  // Har bir dengiz mili 1820 metr
        return dist/1000
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(deg: Double): Double {
        return deg * 180.0 / Math.PI
    }
}