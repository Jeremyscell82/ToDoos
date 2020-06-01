package com.lloydsbyte.todoos.utilz.network

import android.content.Context
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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

    data class ApiModelWoId(
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

    data class DeleteApiResult(
        @SerializedName("count")
        val count: Int
    )


    interface ApiService {

        //Get all todoos from specific user
        @GET("todos")
        fun getAllTodoos(
            @Header("Authorization") token: String,
            @Query("filter") endpoint: String): Observable<List<ApiResult>>

        //Add a Todoo item
        @Headers("Content-Type: application/json")
        @PUT("todos")
        fun postTodo(
            @Header("Authorization") token: String,
            @Body params: ApiResult
        ): Observable<ApiResult>

        //Update a Todoo item
        @Headers("Content-Type: application/json")
        @PUT("todos")
        fun postNewTodo(
            @Header("Authorization") token: String,
            @Body params: ApiModelWoId
        ): Observable<ApiResult>

        //Delete a Todoo
        @Headers("Content-Type: application/json")
        @DELETE("todos/{user}")
        fun deleteTodo(@Path("user") id: String): Observable<DeleteApiResult>


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