package com.example.empcarhelper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.order_row.view.*


class OrdersAdapter(var con:Context, var list:ArrayList<Orders>) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       var v=LayoutInflater.from(con).inflate(R.layout.order_row, parent, false)
        return OrderHolder(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderHolder).show(list[position].name,list[position].order_num,list[position].order_date,list[position].order_status)
        holder.itemView.order_no.setOnClickListener{
            var i=Intent(con, OrderDetAct::class.java)
            EmpInfo.order_num=list[position].order_num
            con.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }



    class OrderHolder(itemView:View) :RecyclerView.ViewHolder(itemView)
    {

        fun show(name:String,orderNo:Int,orderDate:String,status:String)
        {
            itemView.service_name.text=name
            itemView.order_no.text=orderNo.toString()
            itemView.order_date.text=orderDate
            itemView.order_status.text=status
        }
    }


}