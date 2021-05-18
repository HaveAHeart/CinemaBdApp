package com.example.cinemabdapp

class UtilityClass {
    companion object {

        const val SPF_NAME = "infoBD"
        const val IP_NAME = "ip"

        const val CHECK_CONNECTION = "http://%s:3000/movie?limit=1"

        const val GET_MOVIE_BY_ID = "http://%s:3000/movie?id=eq.%s"
        const val GET_NEAREST_FILMS = "http://%s:3000/rpc/getnearestfilms"
        const val GET_PERSONS_BY_MOVIEID = "http://%s:3000/rpc/getpersons?mid=%s"
        const val GET_SESSIONS_BY_MOVIEID = "http://%s:3000/rpc/getsessions?inmid=%s"
        const val GET_PLACES_BY_SESSIONID = "http://%s:3000/rpc/getplaces?inmsid=%s"
        const val GET_HALLID_BY_HALLNAME = "http://%s:3000/hall?select=id&hallname=eq.%s"
        const val SEARCH_MOVIES_BY_PATTERN = "http://%s:3000/rpc/searchmovies?pattern=%s"

        const val BUY_TICKET = "http://%s:3000/rpc/buyticket"
        const val DELETE_TICKET = "http:%s:3000/rpc/deleteticket"
        const val MOVIE_TABLE = "http://%s:3000/movie"
        const val MOVIESESSION_TABLE = "http://%s:3000/moviesession"

        const val PATCH_MOVIESESSION_BY_ID = "http://%s:3000/moviesession?id=eq.%s"
        const val PATCH_MOVIE_BY_ID = "http://%s:3000/movie?id=eq.%s"
    }
}