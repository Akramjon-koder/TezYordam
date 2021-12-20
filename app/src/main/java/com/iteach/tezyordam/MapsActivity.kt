package com.iteach.tezyordam

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.iteach.tezyordam.base.LocationBase
import com.iteach.tezyordam.base.Person
import com.iteach.tezyordam.databinding.ActivityMapsBinding
import com.iteach.tezyordam.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var fusetLocatonProviderClient: FusedLocationProviderClient
    private val personCollectRef = Firebase.firestore.collection("where")

    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.let { locations->
                for (location in locations){
                    if (location!=null){
                        saveLocation(LocationBase(location.latitude,location.longitude))
                        //Toast.makeText(this@MapsActivity,location.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun saveLocation(location: LocationBase) = CoroutineScope(Dispatchers.IO).launch {
        try {
            personCollectRef
                .document("location")
                .set(location)
//                .addOnSuccessListener {
//                    Toast.makeText(this@MapsActivity,"success",Toast.LENGTH_LONG).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this@MapsActivity,it.message,Toast.LENGTH_LONG).show()
//                }

        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@MapsActivity,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun startCalcilating(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { return }
        val request = LocationRequest.create().apply {
            interval = Constants.CALCULATING_INTERVAL
            fastestInterval = Constants.FASTED_LOCATION_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        Looper.myLooper()?.let {
            fusetLocatonProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                it
            )
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        inits()
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        startCalcilating()
        realtimeUpdates()
    }

    @SuppressLint("VisibleForTests")
    private fun inits() {
        fusetLocatonProviderClient = FusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
        //startCalcilating()

    }

    private fun realtimeUpdates(){
        personCollectRef.addSnapshotListener { value, error ->
            value.let {
                if (it!=null){
                    for (document in it.documents){
                        binding.location.text =document.toObject<LocationBase>()?.latitude.toString()
                    }
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

}