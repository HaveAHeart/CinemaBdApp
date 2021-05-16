package com.example.cinemabdapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemabdapp.R

class RowRecyclerAdapter(private val rowsInfo: HashMap<Int, ArrayList<Int?>>, private val nav: NavController,
                         private val price: String, private val hall: String, private val time: String,
                         private val name: String, private val id: String, private val movTo: Int):
    RecyclerView.Adapter<RowRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeRW: RecyclerView? = null
        var layoutManager: LinearLayoutManager? = null

        init {
            placeRW = itemView.findViewById(R.id.placeList)
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val i = rowsInfo.keys.sorted()[position]
        //Toast.makeText(holder.placeRW?.context, rowsInfo[i].toString() + " for " + rowsInfo.toString(), Toast.LENGTH_SHORT).show()
        holder.placeRW!!.layoutManager = holder.layoutManager
        holder.placeRW!!.adapter = SeatRecyclerAdapter(rowsInfo[i]!!, nav, i, price, hall, time, name, id, movTo)
    }

    override fun getItemCount(): Int {
        return rowsInfo.keys.size
    }
}