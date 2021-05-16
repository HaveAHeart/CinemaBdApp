package com.example.cinemabdapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemabdapp.R
import org.json.JSONArray

class PersonsRecyclerAdapter(private val persons: JSONArray, private val nav: NavController):
    RecyclerView.Adapter<PersonsRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var personName: TextView? = null
        var personRole: TextView? = null
        var personId: TextView? = null

        init {
            personName = itemView.findViewById(R.id.itemPersonName)
            personRole = itemView.findViewById(R.id.itemPersonRole)
            personId = itemView.findViewById(R.id.itemPersonId)

            /*itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", personId?.text.toString())
                nav.navigate(R.id.userFragmentFilm, bundle)
            }*/
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

        holder.personId?.text = id
        holder.personName?.text = "$name $surname"
        holder.personRole?.text = "$role"
    }

    override fun getItemCount(): Int {
        return persons.length()
    }
}