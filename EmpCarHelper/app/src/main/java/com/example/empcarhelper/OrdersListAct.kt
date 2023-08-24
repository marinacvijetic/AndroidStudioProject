package com.example.empcarhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_orders_list.*
import kotlinx.android.synthetic.main.order_row.*
private const val TAG = "OrdersListAct"
class OrdersListAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_list)



        var url=EmpInfo.url + "show_orders.php?mobile=" + EmpInfo.mobile
        var list = ArrayList<Orders>()

        var rq= Volley.newRequestQueue(this)
        var jar=JsonArrayRequest(Request.Method.GET, url, null,
        Response.Listener { response ->
            for(x in 0..response.length()-1)
                list.add(Orders(response.getJSONObject(x).getString("name"),
                    response.getJSONObject(x).getInt("order_num"),
                    response.getJSONObject(x).getString("order_date"),
                    response.getJSONObject(x).getString("order_status")))

            var adp=OrdersAdapter(this, list)
            orders_rv.adapter=adp
            orders_rv.layoutManager=LinearLayoutManager(this)
        }, Response.ErrorListener {  })

        rq.add(jar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            Log.i(TAG, "User wants to logout")
            startActivity(Intent(this, MainActivity::class.java))

        }
        return super.onOptionsItemSelected(item)
    }
}