package com.lloydsbyte.todoos.utilz.network

import android.content.Context
import com.lloydsbyte.todoos.R
import com.lloydsbyte.todoos.utilz.SharedPref_Controller
import timber.log.Timber

class NetworkConstants {
    companion object {
        fun baseUrl(context: Context): String {
            val url = context.resources.getString(
                R.string.baseUrl,
                SharedPref_Controller(context).getServerAddress()
            )
            Timber.d("JL_ url is : $url")
            return url
        }
        //Todo Replace with your values
        val SERVER_IP = "00.00.0.000"
        val SERVER_WEB_ADDRESS = "example.com"
        val PORT = "8282"
        val usersEndpoint: String = "Users/"
    }
}