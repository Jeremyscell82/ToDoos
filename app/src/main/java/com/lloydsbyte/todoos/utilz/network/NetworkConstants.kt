package com.lloydsbyte.todoos.utilz.network

import android.content.Context
import com.lloydsbyte.todoos.R
import com.lloydsbyte.todoos.utilz.SharedPref_Controller

class NetworkConstants {
    companion object {
        fun baseUrl(context: Context): String {
            return context.resources.getString(
                R.string.baseUrl,
                SharedPref_Controller(context).getServerAddress()
            )
        }
        //Todo Replace with your values
        val SERVER_IP = "75.70.179.231"
        val SERVER_WEB_ADDRESS = "nextcloud.lloydsbyte.com"
        val PORT = "8282"
        //User this vs baseUrl
        val internalUrl: String = "http://192.168.1.169:8282/api/"
        val usersEndpoint: String = "Users/"
    }
}