package com.example.carhelperapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_order.*


class OrderAct : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var lon:Double=0.0
    var lat:Double=0.0


    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    @SuppressLint("Missing permission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        fun sendSMS()
//        {
//            val uri = Uri.parse("smsto:0645814997")
//            val intent = Intent(Intent.ACTION_SENDTO, uri)
//            intent.putExtra("sms_body", "Here goes your message...")
//            startActivity(intent)
//        }

        if(Build.VERSION.SDK_INT<26)
            setUpMap()
        else
            ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)



        order_button.setOnClickListener {
            if(Build.VERSION.SDK_INT<26) {
                sendOrder()

            }
            else
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS), 456)
        }


    }

    fun sendOrder(){
        var url:String=UserInfo.url + "avl_emp.php?mobile=" + UserInfo.mobile +
                "&service_id=" + UserInfo.service_id + "&lon=" + lon + "&lat=" + lat

        var rq= Volley.newRequestQueue(this)
        var sr= JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener
            {
                    response ->
                run {
                    if(response.has("status") && response["status"] != -1) {
                        Toast.makeText(
                            this,
                            "Your order has been sent", Toast.LENGTH_LONG
                        ).show()
                        try {
                            var mgr = SmsManager.getDefault() as SmsManager
                            mgr.sendTextMessage(
                                response.getString("emp_mobile"),
                                null,
                                "You have a new task " + response.getString("order_num"),
                                null,
                                null
                            )
                        } catch (e: Exception) {
                            println(e.stackTrace);
                        }
                    } else {

                        Toast.makeText(
                            this,
                            "There aren't any available employees to process the order, please try again later!", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            Response.ErrorListener {  })


        rq.add(sr)

    }






    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()

    }

    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)

            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this){ location ->
            if (location != null){

                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 16f))
            }

            lon=location.longitude
            lat=location.latitude
        }

    }

    private fun placeMarkerOnMap(currentLatLong: LatLng){
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker) = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
        ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                setUpMap()
        }
        if(requestCode==456)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                sendOrder()
        }
    }


}