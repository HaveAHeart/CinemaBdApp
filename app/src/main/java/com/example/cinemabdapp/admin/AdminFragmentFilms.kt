package com.example.cinemabdapp.admin

import com.example.cinemabdapp.adapters.FilmsRecyclerAdapter
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
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.android.volley.toolbox.Volley.newRequestQueue
import com.example.cinemabdapp.R
import com.example.cinemabdapp.UtilityClass
import com.example.cinemabdapp.UtilityClass.Companion.GET_NEAREST_FILMS
import com.example.cinemabdapp.UtilityClass.Companion.SEARCH_MOVIES_BY_PATTERN
import com.example.cinemabdapp.UtilityClass.Companion.getErrorMsg
import kotlinx.android.synthetic.main.fragment_admin_films.*
import kotlinx.android.synthetic.main.fragment_user_films.buttonFilmSearch
import kotlinx.android.synthetic.main.fragment_user_films.editTextSearchFilm
import kotlinx.android.synthetic.main.fragment_user_films.filmsList


class AdminFragmentFilms: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_films, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val cache = DiskBasedCache(context?.cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        val queue: RequestQueue = RequestQueue(cache, network).apply{
            start()
        }

        val spf: SharedPreferences = requireActivity().getSharedPreferences(UtilityClass.SPF_NAME, Context.MODE_PRIVATE)
        val ipDB = spf.getString(UtilityClass.IP_NAME, null)
        val nav = Navigation.findNavController(requireView())
        val t = System.nanoTime()

        val request = JsonArrayRequest(
            GET_NEAREST_FILMS.format(ipDB),
            Response.Listener { jsonArr ->
                Toast.makeText(context, (System.nanoTime() - t).toString(), Toast.LENGTH_LONG).show()
                filmsList.layoutManager = LinearLayoutManager(activity)
                filmsList.adapter = FilmsRecyclerAdapter(jsonArr, nav, R.id.adminFragmentFilm)

            },
            Response.ErrorListener {
                Toast.makeText(
                    requireActivity(),
                    getErrorMsg(it),
                    Toast.LENGTH_LONG
                ).show()
            }
        )
        queue.add(request)

        buttonFilmSearch.setOnClickListener {
            val requestSearch = JsonArrayRequest(
                SEARCH_MOVIES_BY_PATTERN.format(ipDB, editTextSearchFilm.text.toString()),
                Response.Listener { jsonArr ->
                    filmsList.layoutManager = LinearLayoutManager(activity)
                    filmsList.adapter = FilmsRecyclerAdapter(jsonArr, nav, R.id.adminFragmentFilm)

                },
                Response.ErrorListener {
                    Toast.makeText(
                        requireActivity(),
                        getErrorMsg(it),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            queue.add(requestSearch)
        }

        buttonFilmAdd.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "0")
            bundle.putString("action", "create")
            nav.navigate(R.id.adminFragmentFilm, bundle)
        }


    }


}
