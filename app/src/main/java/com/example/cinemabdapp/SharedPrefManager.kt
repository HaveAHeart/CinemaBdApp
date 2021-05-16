package com.example.cinemabdapp

import android.content.Context
import android.content.SharedPreferences


class SharedPrefManager {
    companion object {
        val SPF_NAME = "infoBD"
        val IP_NAME = "ip"
        private var mInstance: SharedPreferences? = null
        private var ctx: Context? = null
        fun init(ctx: Context) {
            Companion.ctx = ctx
            mInstance = ctx.getSharedPreferences("infoBD", Context.MODE_PRIVATE)
        }

        fun putIP(ip: String) {
            val editor = mInstance!!.edit()
            editor.putString("ip", ip)
            editor.apply()
        }

        fun getIP(): String {
            return mInstance!!.getString("ip", null)!!
        }
    }
}