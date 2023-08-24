package com.example.carhelperapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_login = findViewById<TextView>(R.id.button_login);
        val login_mobile = findViewById<EditText>(R.id.login_mobile);
        val login_password = findViewById<EditText>(R.id.login_password);
        val login_signup = findViewById<TextView>(R.id.login_signup);
        button_login.setOnClickListener{
            var url=UserInfo.url + "login.php?mobile=" + login_mobile.text.toString() + "&password=" + login_password.text.toString()
            var rq=Volley.newRequestQueue(this)
            var sr=StringRequest(Request.Method.GET, url,
                Response.Listener{ response ->
                    if(response=="0")
                        Toast.makeText(this, "Login Failed",Toast.LENGTH_LONG).show()
                    else
                    {
                        UserInfo.mobile=login_mobile.text.toString()
                        var i=Intent(this, HomeAct::class.java)
                        startActivity(i)
                    }
                },
               Response.ErrorListener {  })
            rq.add(sr)
        }

        login_signup.setOnClickListener {
            var i= Intent(this, SignUpAct::class.java)
            startActivity(i)
        }




    }

}