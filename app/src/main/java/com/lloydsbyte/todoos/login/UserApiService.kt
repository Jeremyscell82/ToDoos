package com.lloydsbyte.todoos.login

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.lloydsbyte.todoos.utilz.network.NetworkConstants
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

class UserApiService {

    data class ApiUserResult(
        @SerializedName("username")
        val username: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("emailVerified")
        val emailVerified: Boolean
    )

    interface ApiService {

        @Headers("Content-Type: application/json")
        @GET
        fun getUser(
            @Header("Authorization") token: String,
            @Url endpoint: String
        ): Observable<ApiUserResult>

        companion object {
            //Todo consolidate the following... soon
            fun createUserService(context: Context): ApiService {
                val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstants.baseUrl(context))
                    .build()
                return retrofit.create(ApiService::class.java)
            }
        }
    }
}