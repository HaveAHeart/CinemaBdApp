package com.example.cinemabdapp.admin

import android.content.Context
import android.os.Bundle
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
import com.example.cinemabdapp.UtilityClass
import com.example.cinemabdapp.UtilityClass.Companion.CHECK_CONNECTION
import kotlinx.android.synthetic.main.fragment_admin_login.*

class AdminFragmentLogin: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val spf = requireActivity().getSharedPreferences(UtilityClass.SPF_NAME, Context.MODE_PRIVATE)
        editTextIP.setText(spf.getString(UtilityClass.IP_NAME, null))

        buttonAdminLogging.setOnClickListener {
            val inputIP = editTextIP.text.toString()
            val queue = Volley.newRequestQueue(requireActivity())

            val request = JsonArrayRequest(
                CHECK_CONNECTION.format(inputIP),
                Response.Listener {
                    val ed = spf.edit()
                    ed.putString(UtilityClass.IP_NAME, inputIP)
                    ed.apply()

                    Navigation.findNavController(requireView()).navigate(R.id.adminFragmentFilms)
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