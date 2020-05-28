package com.lloydsbyte.todoos.network

import android.content.Context
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

class ToDoosApiService {

    data class ApiResult(
        @SerializedName("id")
        val cloudId: String,
        @SerializedName("user")
        val userId: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("completed")
        val completed: Boolean,
        @SerializedName("url")
        val url: String
        )

    interface ApiService {

        //Get All ToDoos
        @GET("todo/")
        fun getAllTodoos(): Observable<List<ApiResult>>

        //Post a new Todo
        @Headers("Content-Type: application/json")
        @POST("todo/")
        fun postTodo(
            @Body params: Map<String, String>
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