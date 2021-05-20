package com.example.cinemabdapp.user

import com.example.cinemabdapp.adapters.RowRecyclerAdapter
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
import com.example.cinemabdapp.R
import com.example.cinemabdapp.UtilityClass
import com.example.cinemabdapp.UtilityClass.Companion.GET_PLACES_BY_SESSIONID
import com.example.cinemabdapp.UtilityClass.Companion.getErrorMsg
import kotlinx.android.synthetic.main.fragment_user_session.*


class UserFragmentSession: Fragment() {
    var id: String? = null
    var name: String? = null
    var time: String? = null
    var hall: String? = null
    var price: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_session, container, false)
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

        id = arguments?.getString("id")
        name = arguments?.getString("name")
        time = textTime.text.toString() + arguments?.getString("time")
        hall = arguments?.getString("hall")
        price = textPrice.text.toString() + arguments?.getString("price")

        textName.text = name
        textTime.text = time
        textHall.text = hall
        textPrice.text = price

        val request = JsonArrayRequest(
            GET_PLACES_BY_SESSIONID.format(ipDB, id),
            Response.Listener { jsonArr ->

                //forming a hashmap, where for every row as key there will be
                // ArrayList of places as value, with null for places that are free


                val rowsInfo = HashMap<Int, ArrayList<Int?>>()
                for (i in 0 until jsonArr.length()) {
                    val jsonObj = jsonArr.getJSONObject(i)
                    val row = jsonObj.getInt("rw")
                    val seatStr = jsonObj.getString("st")
                    val seat: Int?
                    seat = if (seatStr == "null") null else Integer.parseInt(seatStr)

                    if (rowsInfo[row] == null) {
                        val totalSeats = jsonObj.getInt("ttl")
                        rowsInfo[row] = ArrayList()
                        for (i in 0..totalSeats) rowsInfo[row]?.add(null)
                    }
                    if (seat != null) rowsInfo[row]?.add(seat - 1, 1)
                }

                rowsList.layoutManager = LinearLayoutManager(activity)
                rowsList.adapter = RowRecyclerAdapter(rowsInfo, nav, price!!, hall!!, time!!, name!!, id!!, R.id.userFragmentTicket)


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
    }
}
