package com.lloydsbyte.todoos.utilz.network

import android.content.Context
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class CreateAcctApiService {

    data class ApiResult(
        @SerializedName("username")
        val userName: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val userId: String
    )

    data class ApiModel(
        @SerializedName("realm")
        val realm: String,
        @SerializedName("username")
        val userName: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("emailVerified")
        val emailVerified: Boolean,
        @SerializedName("password")
        val password: String
    )

    interface ApiService {

        @Headers("Content-Type: application/json", "Accept: application/json")
        @POST("Users")
        fun createUser(
            @Body params: ApiModel
        ): Observable<ApiResult>

        companion object {
            fun create(context: Context): ApiService {
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