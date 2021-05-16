package com.example.cinemabdapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemabdapp.R

class SeatRecyclerAdapter(private val row: java.util.ArrayList<Int?>, private val nav: NavController, private val rowNum: Int,
                          private val price: String, private val hall: String, private val time: String, private val name: String,
                          private val id: String, private val movTo: Int):
    RecyclerView.Adapter<SeatRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeCircle: ImageView? = null
        var pos: Int? = null
        var isTaken: Boolean? = null
        init {
            placeCircle = itemView.findViewById(R.id.placeCircle)

            itemView.setOnClickListener {
                val bundle = Bundle()
                //Toast.makeText(context, "$pos", Toast.LENGTH_LONG).show()
                bundle.putString("seat", pos.toString())
                bundle.putString("row", rowNum.toString())
                bundle.putString("price", price)
                bundle.putString("hall", hall)
                bundle.putString("time", time)
                bundle.putString("name", name)
                bundle.putString("id", id)
                bundle.putString("isTaken", isTaken.toString())
                nav.navigate(movTo, bundle)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_place, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Toast.makeText(holder.placeCircle?.context, row.toString() + " at " + position.toString(), Toast.LENGTH_SHORT).show()
        holder.pos = position
        if (row[position] != null) {
            holder.isTaken = true
            holder.placeCircle?.setImageResource(R.drawable.circle_red)
        }
        else holder.isTaken = false
    }

    override fun getItemCount(): Int {
        return row.size
    }
}