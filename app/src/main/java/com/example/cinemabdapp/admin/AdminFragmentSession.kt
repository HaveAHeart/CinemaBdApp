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
import com.android.volley.Request.Method.*
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.cinemabdapp.R
import com.example.cinemabdapp.UtilityClass
import com.example.cinemabdapp.UtilityClass.Companion.GET_HALLID_BY_HALLNAME
import com.example.cinemabdapp.UtilityClass.Companion.GET_PLACES_BY_SESSIONID
import com.example.cinemabdapp.UtilityClass.Companion.MOVIESESSION_TABLE
import com.example.cinemabdapp.UtilityClass.Companion.PATCH_MOVIESESSION_BY_ID
import com.example.cinemabdapp.adapters.RowRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_admin_session.*
import kotlinx.android.synthetic.main.fragment_user_session.rowsList
import kotlinx.android.synthetic.main.fragment_user_session.textHall
import kotlinx.android.synthetic.main.fragment_user_session.textName
import kotlinx.android.synthetic.main.fragment_user_session.textPrice
import kotlinx.android.synthetic.main.fragment_user_session.textTime
import org.json.JSONArray


class AdminFragmentSession: Fragment() {
    var id: String? = null
    var name: String? = null
    var time: String? = null
    var hall: String? = null
    var price: String? = null
    var act: String? = null
    var movieId: String? = null
    var hallId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_session, container, false)
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
        hall = arguments?.getString("hall")!!.split(" ")[1]
        price = arguments?.getString("price")!!.split(" ")[0]
        act = arguments?.getString("action") ?: "update"
        movieId = arguments?.getString("mid")

        textName.text = name
        textTime.text = time
        textHall.text = hall
        textPrice.text = price

        if (act == "update") {
            val request = JsonArrayRequest(
                GET_PLACES_BY_SESSIONID.format(ipDB, id),
                Response.Listener { jsonArr ->
                    val rowsInfo = HashMap<Int, ArrayList<Int?>>()

                    //forming a hashmap, where for every row as key there will be
                    // ArrayList of places as value, with null for places that are free

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
                    rowsList.adapter = RowRecyclerAdapter(rowsInfo, nav, price!!, hall!!, time!!, name!!, id!!, R.id.adminFragmentTicket)


                },
                Response.ErrorListener {
                    Toast.makeText(
                        requireActivity(),
                        "There is no such session. Try again with another one.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            queue.add(request)
        }

        buttonCheckHall.setOnClickListener {
            val reqHall = StringRequest(
                GET,
                GET_HALLID_BY_HALLNAME.format(ipDB, textHall.text.toString()),
                Response.Listener {
                    //Toast.makeText(context, "$it\n${JSONArray(it).getJSONObject(0)}\n${JSONArray(it).getJSONObject(0).getString("id")}", Toast.LENGTH_LONG).show()
                    hallId = JSONArray(it).getJSONObject(0).getString("id")
                },
                Response.ErrorListener {
                    Toast.makeText(context, "unable to get hallid", Toast.LENGTH_LONG).show()
                }
            )

            queue.add(reqHall)
        }

        buttonPushSession.setOnClickListener {
            val reqType = if (act == "update") PATCH else POST
            val url = if (act == "update") PATCH_MOVIESESSION_BY_ID.format(id) else MOVIESESSION_TABLE.format(ipDB)
            val request = object : StringRequest(
                reqType,
                url,
                Response.Listener {
                    Toast.makeText(context, "yay, moviesession updated!", Toast.LENGTH_LONG).show()

                },
                Response.ErrorListener {
                    it.printStackTrace()
                    Toast.makeText(context, "This time is already occupied.", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    //if (act == "update") params["id"] = id.toString()

                    params["hallid"] = hallId!!.toString()
                    params["sessiontime"] = textTime.text.toString()
                    params["sessionprice"] = textPrice.text.toString()
                    return params
                }
            }

            queue.add(request)
        }
    }
}
