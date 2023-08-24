package com.example.empcarhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login_password=findViewById<EditText>(R.id.login_password)
        val login_mobile=findViewById<EditText>(R.id.login_mobile)
        val login_button = findViewById<Button>(R.id.login_button);

        login_button.setOnClickListener{
            var url:String=EmpInfo.url + "login_emp.php?mobile=" +
                    login_mobile.text.toString() + "&password=" +
                    login_password.text.toString()

            var rq=Volley.newRequestQueue(this)
            var sr=StringRequest(Request.Method.GET,url, Response.Listener { response ->
                if(response=="0")
                    Toast.makeText(this, "Login Failed",
                    Toast.LENGTH_LONG).show()
                else
                {
                    EmpInfo.mobile=login_mobile.text.toString()
                    var i=Intent(this, OrdersListAct::class.java)
                    startActivity(i)
                }
            },
            Response.ErrorListener {  })

            rq.add(sr)
        }



    }
}