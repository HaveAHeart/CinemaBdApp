package com.example.cinemabdapp.user

import android.content.Context
import android.os.Bundle
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.cinemabdapp.R
import com.example.cinemabdapp.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_user_login.*
import org.json.JSONArray
import org.json.JSONObject

class UserFragmentLogin: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val spf = requireActivity().getSharedPreferences(SharedPrefManager.SPF_NAME, Context.MODE_PRIVATE)
        editTextIP.setText(spf.getString(SharedPrefManager.IP_NAME, null))

        buttonUserLogging.setOnClickListener {
            val inputIP = editTextIP.text.toString()
            val queue = Volley.newRequestQueue(requireActivity())

            val request = JsonArrayRequest(
                "http://$inputIP:3000/movie?limit=1",
                Response.Listener {
                    val ed = spf.edit()
                    ed.putString(SharedPrefManager.IP_NAME, inputIP)
                    ed.apply()

                    Navigation.findNavController(requireView()).navigate(R.id.userFragmentFilms)
                },
                Response.ErrorListener {
                    Toast.makeText(
                        requireActivity(),
                        "There is no such database at the IP. Try again with another one.",
                        Toast.LENGTH_LONG
                    ).show()
                })
            queue.add(request)

        }
    }
}