package com.example.cinemabdapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService
import com.android.volley.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

class UtilityClass {
    companion object {

        val ROLES = arrayOf("actor", "producer", "operator")

        const val SPF_NAME = "infoBD"
        const val IP_NAME = "ip"

        const val CHECK_CONNECTION = "http://%s:3000/rpc/checkconn"

        const val GET_MOVIE_BY_ID = "http://%s:3000/movie?id=eq.%s"
        const val GET_MOVIEID_BY_INFO = "http://%s:3000/movie?name=eq.%s&releasedate=eq.%s&duration=eq.%s"
        const val GET_NEAREST_FILMS = "http://%s:3000/rpc/getnearestfilms"
        const val GET_PERSONS_BY_MOVIEID = "http://%s:3000/rpc/getpersons?mid=%s"
        const val GET_SESSIONS_BY_MOVIEID = "http://%s:3000/rpc/getsessions?inmid=%s"
        const val GET_PLACES_BY_SESSIONID = "http://%s:3000/rpc/getplaces?inmsid=%s"
        const val GET_HALLID_BY_HALLNAME = "http://%s:3000/hall?select=id&hallname=eq.%s"
        const val SEARCH_MOVIES_BY_PATTERN = "http://%s:3000/rpc/searchmovies?pattern=%s"



        const val PUSH_MOVIE = "http://%s:3000/rpc/pushmovie"
        const val PUSH_SESSION = "http://%s:3000/rpc/pushsession"
        const val PUSH_PERSON = "http://%s:3000/rpc/pushperson"

        const val BUY_TICKET = "http://%s:3000/rpc/buyticket"
        const val DELETE_TICKET = "http:%s:3000/rpc/deleteticket"

        fun getErrorMsg(error: VolleyError): String {
            var errorMsg = ""
            if (error is NoConnectionError) {
                errorMsg = "Can not establish connection between server and device. Please try again."
            } else if (error is NetworkError || error.cause is ConnectException) {
                errorMsg = "Your device is not connected to internet.please try again with active internet connection."
            } else if (error.cause is MalformedURLException) {
                errorMsg = "Bad URL request happened. Please, try again."
            } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
                errorMsg = "Error with data parsing occured. Please try again."
            } else if (error.cause is OutOfMemoryError) {
                errorMsg = "OOM error happens. Check if there are enough memory and try again."
            } else if (error is ServerError || error.cause is ServerError) {
                errorMsg = "Internal server error, mostly connected with wrong input data format. Please, try again."
            } else if (error is TimeoutError || error.cause is SocketTimeoutException || error.cause is ConnectTimeoutException || error.cause is SocketException || (error.cause!!.message != null && error.cause!!.message!!.contains(
                    "Your connection has timed out, please try again."
                ))
            ) {
                errorMsg = "Your connection has timed out, please try again"
            } else {
                errorMsg = "An unknown error occurred during the operation, please try again."
            }
            return errorMsg


        }


    }
}