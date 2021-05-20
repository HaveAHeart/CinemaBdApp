package com.example.cinemabdapp.admin

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
import com.android.volley.Request.Method.POST
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.cinemabdapp.R
import com.example.cinemabdapp.UtilityClass
import com.example.cinemabdapp.UtilityClass.Companion.GET_MOVIEID_BY_INFO
import com.example.cinemabdapp.UtilityClass.Companion.GET_MOVIE_BY_ID
import com.example.cinemabdapp.UtilityClass.Companion.GET_PERSONS_BY_MOVIEID
import com.example.cinemabdapp.UtilityClass.Companion.GET_SESSIONS_BY_MOVIEID
import com.example.cinemabdapp.UtilityClass.Companion.PUSH_MOVIE
import com.example.cinemabdapp.adapters.PersonsRecyclerAdapter
import com.example.cinemabdapp.adapters.SessionsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_admin_film.*
import kotlinx.android.synthetic.main.fragment_admin_film.textDate
import kotlinx.android.synthetic.main.fragment_admin_film.textName
import org.json.JSONArray

class AdminFragmentFilm: Fragment() {
    var act: String? = null
    var id: String? = "0"
    var name: String? = null
    var date: String? = null
    var dur: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_film, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val cache = DiskBasedCache(context?.cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        val queue: RequestQueue = RequestQueue(cache, network).apply {
            start()
        }

        val spf: SharedPreferences =
            requireActivity().getSharedPreferences(UtilityClass.SPF_NAME, Context.MODE_PRIVATE)
        val ipDB = spf.getString(UtilityClass.IP_NAME, null)
        val nav = Navigation.findNavController(requireView())

        id = arguments?.getString("id")
        act = arguments?.getString("action") ?: "update"

        if (id != "0") {

            val request = JsonArrayRequest(
                GET_MOVIE_BY_ID.format(ipDB, id),
                Response.Listener {
                    val movie = it.getJSONObject(0)

                    name = movie.getString("name")
                    date = movie.getString("releasedate")
                    dur = movie.getString("duration")

                    textName.setText(name)
                    textDate.setText(date)
                    textDur.setText(dur)
                },
                Response.ErrorListener {

                    Toast.makeText(
                        requireActivity(),
                        UtilityClass.getErrorMsg(it),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            queue.add(request)

            val requestRoles = JsonArrayRequest(
                GET_PERSONS_BY_MOVIEID.format(ipDB, id),
                Response.Listener<JSONArray> {
                    personsList.layoutManager = LinearLayoutManager(activity)
                    personsList.adapter =
                        PersonsRecyclerAdapter(it, nav, id!!, R.id.adminFragmentPerson)
                },
                Response.ErrorListener {
                    Toast.makeText(
                        requireContext(),
                        UtilityClass.getErrorMsg(it), Toast.LENGTH_LONG
                    ).show()
                }
            )
            queue.add(requestRoles)

            val requestSessions = JsonArrayRequest(
                GET_SESSIONS_BY_MOVIEID.format(ipDB, id),
                Response.Listener<JSONArray> {
                    sessionsList.layoutManager = LinearLayoutManager(activity)
                    sessionsList.adapter =
                        SessionsRecyclerAdapter(it, nav, name!!, R.id.userFragmentSession)
                },
                Response.ErrorListener {
                    Toast.makeText(
                        requireContext(),
                        UtilityClass.getErrorMsg(it), Toast.LENGTH_LONG
                    ).show()
                }
            )
            queue.add(requestSessions)

        }


        buttonPush.setOnClickListener {
            val request = object : StringRequest(
                POST,
                PUSH_MOVIE.format(ipDB),
                Response.Listener<String> {
                    Toast.makeText(context, "yay, movie updated!", Toast.LENGTH_LONG).show()

                    name = textName.text.toString()
                    date = textDate.text.toString()
                    dur = textDur.text.toString()
                },
                Response.ErrorListener {
                    Toast.makeText(
                        context,
                        UtilityClass.getErrorMsg(it),
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()

                    params["inid"] = id ?: "0"
                    params["n"] = textName.text.toString()
                    params["rd"] = textDate.text.toString()
                    params["d"] = textDur.text.toString()
                    return params
                }
            }

            queue.add(request)
        }



        buttonAddSession.setOnClickListener {

            val requestId = JsonArrayRequest(
                GET_MOVIEID_BY_INFO.format(ipDB, name, date, dur),
                Response.Listener<JSONArray> {
                    id = it.getJSONObject(0).getString("id")

                    val bundle = Bundle()
                    bundle.putString("id", "0")
                    bundle.putString("name", name)
                    bundle.putString("time", "1970-01-01 00:00:00")
                    bundle.putString("hall", "default hall")
                    bundle.putString("price", "0")
                    bundle.putString("action", "create")
                    bundle.putString("mid", id)
                    nav.navigate(R.id.adminFragmentSession, bundle)
                },
                Response.ErrorListener {
                    Toast.makeText(
                        requireActivity(),
                        UtilityClass.getErrorMsg(it),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            queue.add(requestId)


        }

        buttonAddPerson.setOnClickListener {
            val requestId = JsonArrayRequest(
                GET_MOVIEID_BY_INFO.format(ipDB, name, date, dur),
                Response.Listener<JSONArray> {
                    id = it.getJSONObject(0).getString("id")

                    val bundle = Bundle()
                    bundle.putString("id", "0")
                    bundle.putString("mid", id)
                    bundle.putString("name", "")
                    bundle.putString("surname", "")
                    bundle.putString("role", "")
                    bundle.putString("dob", "")
                    nav.navigate(R.id.adminFragmentPerson, bundle)
                },
                Response.ErrorListener {
                    Toast.makeText(
                        requireActivity(),
                        UtilityClass.getErrorMsg(it),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            queue.add(requestId)


        }
    }
}
