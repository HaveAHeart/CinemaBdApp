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

class FilmsRecyclerAdapter(private val films: JSONArray, private val nav: NavController, private val navTo: Int):
    RecyclerView.Adapter<FilmsRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var filmName: TextView? = null
        var filmDate: TextView? = null
        var filmID: TextView? = null

        init {
            filmName = itemView.findViewById(R.id.itemFilmName)
            filmDate = itemView.findViewById(R.id.itemFilmDate)
            filmID = itemView.findViewById(R.id.itemFilmID)

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", filmID?.text.toString())

                nav.navigate(navTo, bundle)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_film, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val film = films.getJSONObject(position)
        val dateTime = film.getString("st").split("T")
        val time = dateTime[1].split(":")
        holder.filmName?.text = film.getString("n")
        holder.filmDate?.text = "${dateTime[0]} at ${time[0]}:${time[1]}"
        holder.filmID?.text = film.getString("id")
    }

    override fun getItemCount(): Int {
        return films.length()
    }
}