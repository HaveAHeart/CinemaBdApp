package com.example.cinemabdapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.fragment_login_choose.*
import kotlinx.android.synthetic.main.fragment_user_login.*

class LoginFragmentChoose : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_login_choose, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonUser.setOnClickListener { Navigation.findNavController(it).navigate(R.id.userFragmentLogin) }
        buttonAdmin.setOnClickListener { Navigation.findNavController(it).navigate(R.id.adminFragmentLogin) }
    }
}