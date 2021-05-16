package com.example.cinemabdapp.user

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
import com.example.cinemabdapp.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_user_ticket.*
import org.json.JSONArray


class UserFragmentTicket: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_ticket, container, false)
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

        val id = arguments?.getString("id")
        val time = arguments?.getString("time")
        val hall = arguments?.getString("hall")
        val price = arguments?.getString("price")
        val seat = arguments?.getString("seat")
        val row = arguments?.getString("row")

        val request = object : StringRequest(
            POST,
            "http://$ipDB:3000/rpc/buyticket",
            Response.Listener<String> {
                Toast.makeText(context, "yay, ticket bought!", Toast.LENGTH_LONG).show()
                textID.text = JSONArray(it).getJSONObject(0).getInt("retid").toString()
                textMoney.text = price!!.split(":")[1]
                textHallName.text = hall!!.split(" ")[1]
                textRowNumber.text = row
                textSeatNumber.text = seat
                val times = time!!.split(" ")
                textTime.text = "${times[2]} \n ${times[3]}"
            },
            Response.ErrorListener {
                Toast.makeText(context, "This seat is already taken - don't you see the red color?", Toast.LENGTH_LONG).show()
                nav.popBackStack()
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()

                params["inmsid"] = id.toString()
                params["rw"] = row.toString()
                params["st"] = seat.toString()
                return params
            }
        }

        queue.add(request)



    }





}
