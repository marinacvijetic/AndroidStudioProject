package com.example.empcarhelper

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.empcarhelper.databinding.ActivityOrderDetBinding


class OrderDetAct  : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityOrderDetBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderDetBinding.inflate(layoutInflater)
        setContentView(binding.root)




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(Build.VERSION.SDK_INT >= 26)
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), 1)

    }




    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        setUpMap()


    }

    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)

            return
        }

        var url:String=EmpInfo.url + "order_details.php?order_num=" + EmpInfo.order_num
        var rq=Volley.newRequestQueue(this)
        var jor=JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val custLoc = LatLng(response.getDouble("lat"), response.getDouble("lon"))
                mMap.addMarker(MarkerOptions().position(custLoc).title("Marker on customers location"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(custLoc, 12f))

            }, Response.ErrorListener {  })
        rq.add(jor)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.status_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var status:String=item.title.toString()

        var url:String = EmpInfo.url + "edit_status.php?order_status=" + status +
                "&order_num=" + EmpInfo.order_num
        var rq=Volley.newRequestQueue(this)
        var sr=StringRequest(Request.Method.GET, url,
        Response.Listener { response ->
            Toast.makeText(this, "The order status has been changed",
                            Toast.LENGTH_LONG).show()
        }, Response.ErrorListener {  })

        rq.add(sr)
        return super.onOptionsItemSelected(item)


    }



}