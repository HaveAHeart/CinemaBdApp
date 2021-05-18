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
import com.android.volley.Request.Method.POST
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.cinemabdapp.R
import com.example.cinemabdapp.UtilityClass
import com.example.cinemabdapp.UtilityClass.Companion.DELETE_TICKET
import kotlinx.android.synthetic.main.fragment_admin_ticket.*


class AdminFragmentTicket: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_ticket, container, false)
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

        val id = arguments?.getString("id")
        val time = arguments?.getString("time")
        val hall = arguments?.getString("hall")
        val price = arguments?.getString("price")
        val seat = (arguments?.getString("seat")!!.toInt() + 1).toString()
        val row = arguments?.getString("row")
        val isTaken = arguments?.getString("isTaken")

        textID.text = id
        textMoney.text = price
        textHallName.text = hall
        textRowNumber.text = row
        textSeatNumber.text = seat
        textTime.text = time

        if (isTaken == "true") {
            buttonDeleteTicket.setOnClickListener {
                val request = object : StringRequest(
                    POST,
                    DELETE_TICKET.format(ipDB, id),
                    Response.Listener<String> {
                        Toast.makeText(context, "yay, ticket removed!", Toast.LENGTH_LONG).show()
                        nav.popBackStack()
                    },
                    Response.ErrorListener {
                        Toast.makeText(
                            context,
                            "Experienced the troubles with removing the ticket.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ) {
                    override fun getParams(): Map<String, String>? {
                        val params: MutableMap<String, String> = HashMap()

                        params["inmsid"] = id!!
                        params["rw"] = row!!
                        params["st"] = seat
                        return params
                    }
                }
                queue.add(request)
            }
        } else {
            buttonDeleteTicket.text = "This ticket is yet to be sold."
        }



    }





}
