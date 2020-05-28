package com.lloydsbyte.todoos.network

import android.content.Context
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class LoginApiService {

    data class ApiLoginResult(
        @SerializedName("id")
        val token: String,
        @SerializedName("ttl")
        val ttl: Int, //May not need
        @SerializedName("created")
        val created: String,
        @SerializedName("userId")
        val userId: String
    )


    interface ApiService {

        @Headers("Content-Type: application/json")
        @POST("Users/login")
        fun loginUser(
            @Body params: Map<String, String>
        ): Observable<ApiLoginResult>

        companion object {
            //Todo consolidate the following... soon
            fun createLoginService(context: Context): ApiService {
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