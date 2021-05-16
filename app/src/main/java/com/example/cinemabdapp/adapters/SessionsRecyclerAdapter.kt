package com.example.cinemabdapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemabdapp.R
import org.json.JSONArray

class SessionsRecyclerAdapter(private val sessions: JSONArray, private val nav: NavController, private val movieName: String,
                              private val movTo: Int):
    RecyclerView.Adapter<SessionsRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sessionTime: TextView? = null
        var sessionPrice: TextView? = null
        var sessionHall: TextView? = null
        var sessionFree: TextView? = null
        var sessionId: String? = null
        var movieId: String? = null

        init {
            sessionTime = itemView.findViewById(R.id.itemSessionTime)
            sessionPrice = itemView.findViewById(R.id.itemSessionPrice)
            sessionHall = itemView.findViewById(R.id.itemSessionHall)
            sessionFree = itemView.findViewById(R.id.itemSessionFree)
            sessionId = ""
            movieId = ""

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", sessionId)
                bundle.putString("name", movieName)
                bundle.putString("time", sessionTime?.text.toString())
                bundle.putString("hall", sessionHall?.text.toString())
                bundle.putString("price", sessionPrice?.text.toString())
                bundle.putString("mid", movieId.toString())
                nav.navigate(movTo, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_session, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val session = sessions.getJSONObject(position)
        val time = session.getString("st").split(" ")
        val date = time[0]
        val timeText = "${time[1].split(":")[0]}:${time[1].split(":")[1]}"
        val price = session.getString("sp")
        val hall = session.getString("hn")
        val free = session.getString("fr")
        val id = session.getString("id")
        val mid = session.getString("mid")
        holder.sessionTime?.text = "$date $timeText"
        holder.sessionPrice?.text = "$price ₽"
        holder.sessionHall?.text = "Hall $hall"
        holder.sessionFree?.text = "Seats left: $free"
        holder.sessionId = id
        holder.movieId = mid
    }

    override fun getItemCount(): Int {
        return sessions.length()
    }
}