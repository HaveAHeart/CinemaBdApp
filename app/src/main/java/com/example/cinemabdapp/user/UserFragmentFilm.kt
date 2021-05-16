package com.example.cinemabdapp.user

import com.example.cinemabdapp.adapters.PersonsRecyclerAdapter
import com.example.cinemabdapp.adapters.SessionsRecyclerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Cache
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.cinemabdapp.R
import com.example.cinemabdapp.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_user_film.*
import org.json.JSONArray
import java.util.*

class UserFragmentFilm: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_film, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val cache = DiskBasedCache(context?.cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        val queue: RequestQueue = RequestQueue(cache, network).apply{
            start()
        }

        val spf: SharedPreferences = requireActivity().getSharedPreferences(SharedPrefManager.SPF_NAME, Context.MODE_PRIVATE)
        val ipDB = spf.getString(SharedPrefManager.IP_NAME, null)
        val nav = Navigation.findNavController(requireView())
        var movieName = ""
        val id = arguments?.getString("id")

        val request = JsonArrayRequest(
            "http://$ipDB:3000/movie?id=eq.%s".format(id),
            Response.Listener {
                val movie = it.getJSONObject(0)
                movieName = movie.getString("name")
                textName.text = movie.getString("name")
                textDate.text = textDate.text.toString() + movie.getString("releasedate")
            },
            Response.ErrorListener {
                Toast.makeText(
                    requireActivity(),
                    "There is no movie with such ID.",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
        queue.add(request)

        val requestRoles = JsonArrayRequest(
            "http://$ipDB:3000/rpc/getpersons?mid=%s".format(id),
            Response.Listener<JSONArray> {
                personsList.layoutManager = LinearLayoutManager(activity)
                personsList.adapter = PersonsRecyclerAdapter(it, nav)
            },
            Response.ErrorListener { Toast.makeText(requireContext(), "Error while loading persons", Toast.LENGTH_LONG).show() }
        )
        queue.add(requestRoles)

        val requestSessions = JsonArrayRequest(
            "http://$ipDB:3000/rpc/getsessions?inmid=%s".format(id),
            Response.Listener<JSONArray> {
                sessionsList.layoutManager = LinearLayoutManager(activity)
                sessionsList.adapter = SessionsRecyclerAdapter(it, nav, movieName, R.id.userFragmentSession)
            },
            Response.ErrorListener { Toast.makeText(requireContext(), "Error while loading sessions", Toast.LENGTH_LONG).show() }
        )
        queue.add(requestSessions)


    }


}