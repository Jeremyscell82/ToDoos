package com.lloydsbyte.todoos.network

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

        //User this vs baseUrl
        val internalUrl: String = "http://192.168.1.169:8282/api/"
        val usersEndpoint: String = "Users/"
    }
}