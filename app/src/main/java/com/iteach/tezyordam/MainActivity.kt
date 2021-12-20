package com.iteach.tezyordam

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.iteach.tezyordam.base.Person
import com.iteach.tezyordam.databinding.ActivityMainBinding
import com.mindorks.ridesharing.utils.PermissionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private val personCollectRef = Firebase.firestore.collection("persons")

    private val LOCATION_PERMISSION_REQUEST_CODE = 999
    var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    var number = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSend.setOnClickListener {

//            savePerson(Person("First Name $number","Last Name $number"))
//            number++
            startActivity(Intent(this,MapsActivity::class.java))
        }

        realtimeUpdates()
        grandedPermission()
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
                    for (document in it.documents){
                        binding.firstName.text = "${document.toObject<Person>()?.firstName.toString()} \n ${binding.firstName.text}"
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

    private fun savePerson(person: Person) = CoroutineScope(Dispatchers.IO).launch {
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
}