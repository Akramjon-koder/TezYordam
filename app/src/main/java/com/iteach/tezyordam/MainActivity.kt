package com.iteach.tezyordam

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.iteach.tezyordam.adapter.AdapterRecyclerOrders
import com.iteach.tezyordam.base.Order
import com.iteach.tezyordam.databinding.ActivityMainBinding
import com.mindorks.ridesharing.utils.PermissionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(),AdapterRecyclerOrders.OnItemClickListner {
    private val personCollectRef = Firebase.firestore.collection("persons")

    lateinit var fusetLocatonProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 999
    var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    lateinit var ordersAdapter: AdapterRecyclerOrders
    var lastlocation: Location ?= null

    fun startTimer(){
        val timer = object : CountDownTimer(12000000, 60000) {
            override fun onTick(millisUntilFinished: Long) {
                getLocation()
            }
            override fun onFinish() {
            }
        }
        timer.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inits()
        startTimer()
        getLocation()

        binding.apply {

            recyclerOrders.layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL, false
            )

            ordersAdapter = AdapterRecyclerOrders(this@MainActivity, arrayListOf(),this@MainActivity)
            recyclerOrders.adapter = ordersAdapter
        }
//        binding.buttonSend.setOnClickListener {
//
////            savePerson(Person("First Name $number","Last Name $number"))
////            number++
//            startActivity(Intent(this,MapsActivity::class.java))
//        }

        realtimeUpdates()
        grandedPermission()
    }
    @SuppressLint("VisibleForTests")
    private fun inits() {
        fusetLocatonProviderClient = FusedLocationProviderClient(this)
    }

    private fun grandedPermission() {
        var permission = true

        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        permission = false
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun realtimeUpdates(){
        personCollectRef.addSnapshotListener { value, error ->
            value.let {
                if (it!=null){
                    var list:ArrayList<Order?> = arrayListOf()

                    for (document in it.documents){
                        val objectitem:Order
                        if (document.toObject<Order>()!=null){
                            list.add(document.toObject())

                        }

                    }

                    binding.linearProgress.visibility = View.GONE

                    ordersAdapter.setData(lastlocation,list)
                }

            }

            error.let {
                if (it!=null){
                    Toast.makeText(this,it?.message.toString(),Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
            }
        }
    }

    private fun getLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusetLocatonProviderClient.lastLocation.addOnSuccessListener {
            lastlocation = it
        }
    }

    private fun saveOrder(person: Order) = CoroutineScope(Dispatchers.IO).launch {
        try {
            personCollectRef
                .add(person)
                .addOnSuccessListener {
                    Toast.makeText(this@MainActivity,"success",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity,it.message,Toast.LENGTH_LONG).show()
                }


        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun toast(s: String) {
        withContext(Dispatchers.Main){
            Toast.makeText(this@MainActivity,"success",Toast.LENGTH_LONG).show()
        }
    }

    override fun orderClicked(order: Order) {
        val intent = Intent(this,MapsActivity::class.java)
        intent.putExtra("phone",order.phone)
        startActivity(intent)
    }


}