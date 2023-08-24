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

class SignUpAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val signup_button = findViewById<TextView>(R.id.signup_button);
        val signup_mobile = findViewById<EditText>(R.id.signup_mobile);
        val signup_password = findViewById<EditText>(R.id.signup_password);
        val signup_confirm = findViewById<EditText>(R.id.signup_confirm);
        val signup_name = findViewById<EditText>(R.id.signup_name);

        signup_button.setOnClickListener {
            if(signup_password.text.toString()==signup_confirm.text.toString())
            {
                var url:String=UserInfo.url + "signup.php?mobile=" +signup_mobile.text.toString() +
                        "&password=" + signup_password.text.toString() + "&name=" + signup_name.text.toString()
                var rq=Volley.newRequestQueue(this)
                var sr=StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    if(response=="0")
                        Toast.makeText(this, "Mobile Number Already Exist",
                            Toast.LENGTH_LONG).show()
                    else {
                        UserInfo.mobile=signup_mobile.text.toString()
                        var i= Intent(this, HomeAct::class.java)
                        startActivity(i)
                    }

                }, Response.ErrorListener {  })
                rq.add(sr)
            }
            else
                Toast.makeText(this, "Password not match",
                    Toast.LENGTH_LONG).show()
        }
    }
}