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

class PersonsRecyclerAdapter(private val persons: JSONArray, private val nav: NavController, private val mid: String, private val navTo: Int):
    RecyclerView.Adapter<PersonsRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var personName: TextView? = null
        var personRole: TextView? = null
        var personId: String? = null
        var personDOB: String? = null
        var personFName: String? = null
        var personLName: String? = null

        init {
            personName = itemView.findViewById(R.id.itemPersonName)
            personRole = itemView.findViewById(R.id.itemPersonRole)

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", personId)
                bundle.putString("mid", mid)
                bundle.putString("name", personFName)
                bundle.putString("surname", personLName)
                bundle.putString("role", personRole?.text.toString())
                bundle.putString("dob", personDOB)
                if (navTo != 0) nav.navigate(navTo, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_person, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val person = persons.getJSONObject(position)
        val name = person.getString("nameret")
        val surname = person.getString("surnameret")
        val role = person.getString("roleret")
        val id = person.getString("id")
        val dob = person.getString("dateofbirthret")
        holder.personId = id
        holder.personName?.text = "$name $surname"
        holder.personRole?.text = role
        holder.personDOB = dob
        holder.personFName = name
        holder.personLName = surname
    }

    override fun getItemCount(): Int {
        return persons.length()
    }
}