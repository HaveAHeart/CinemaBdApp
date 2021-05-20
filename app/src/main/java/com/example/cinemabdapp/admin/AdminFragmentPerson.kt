package com.example.cinemabdapp.admin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Request.Method.POST
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.example.cinemabdapp.R
import com.example.cinemabdapp.UtilityClass
import com.example.cinemabdapp.UtilityClass.Companion.ROLES
import com.example.cinemabdapp.UtilityClass.Companion.getErrorMsg
import kotlinx.android.synthetic.main.fragment_admin_person.*

class AdminFragmentPerson: Fragment() {
    var id: String? = null
    var mid: String? = null
    var name: String? = null
    var surname: String? = null
    var dob: String? = null
    var role: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_person, container, false)
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
        mid = arguments?.getString("mid")
        name = arguments?.getString("name")
        surname = arguments?.getString("surname")
        role = arguments?.getString("role")
        dob = arguments?.getString("dob")



        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ROLES)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        val itemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                role = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                role = "actor"
            }
        }
        spinnerRole.onItemSelectedListener = itemSelectedListener

        textNameIn.setText(name)
        textSurnameIn.setText(surname)
        textDateOfBirthIn.setText(dob)


        buttonPushPerson.setOnClickListener {

            name = textNameIn.text.toString()
            surname = textSurnameIn.text.toString()
            dob = textDateOfBirthIn.text.toString()

            val request = object : StringRequest(
                POST,
                UtilityClass.PUSH_PERSON.format(ipDB),
                Response.Listener<String> {
                    Toast.makeText(context, "yay, person updated!", Toast.LENGTH_LONG).show()

                },
                Response.ErrorListener {
                    Toast.makeText(
                        context,
                        getErrorMsg(it),
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()

                    params["inid"] = id!!
                    params["n"] = name!!
                    params["sn"] = surname!!
                    params["dob"] = dob!!
                    params["r"] = role!!
                    params["mid"] = mid!!
                    return params
                }
            }

            queue.add(request)
        }
    }




}