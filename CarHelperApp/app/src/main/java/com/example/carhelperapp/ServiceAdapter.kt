package com.example.carhelperapp


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.service_row.view.*

class ServiceAdapter (var con: Context, var list:ArrayList<CarServices>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var v=LayoutInflater.from(con).inflate(R.layout.service_row, parent,false)
            return ServiceHolder(v)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ServiceHolder).show(list[position].name,
            list[position].photo)

            holder.itemView.service_photo.setOnClickListener{
                var i=Intent(con, OrderAct::class.java)
                UserInfo.service_id=list[position].id.toString()
                con.startActivity(i)
            }
        }

        class ServiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


            fun show(n: String, p: String) {
                itemView.service_name.text=n
                Picasso.get().load(UserInfo.url + p).into(itemView.service_photo)
            }
        }

}
