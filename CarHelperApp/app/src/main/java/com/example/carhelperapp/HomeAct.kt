package com.example.carhelperapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_home.*

private const val TAG="HomeAct"
class HomeAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var url:String = UserInfo.url + "services.php"
        var list=ArrayList<CarServices>()

        var rq = Volley.newRequestQueue(this)
        var jar=JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
            for(x in 0..response.length()-1)
                list.add(CarServices(
                    response.getJSONObject(x).getInt("id"),
                    response.getJSONObject(x).getString("name"),
                    response.getJSONObject(x).getString("photo")))

            var adp=ServiceAdapter(this,list)
            services_rv.adapter=adp
            services_rv.layoutManager=GridLayoutManager(this, 2)

        }, Response.ErrorListener {  })
        rq.add(jar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.logout){
            Log.i(TAG, "User wants to logout")
            startActivity(Intent(this, MainActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}