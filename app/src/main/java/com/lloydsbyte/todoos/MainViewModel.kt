package com.lloydsbyte.todoos

import androidx.lifecycle.ViewModel
import com.lloydsbyte.todoos.database.AppDatabase
import com.lloydsbyte.todoos.home.TodoItemModel
import com.lloydsbyte.todoos.login.UserApiService
import com.lloydsbyte.todoos.network.LoginApiService
import io.reactivex.Flowable

class MainViewModel: ViewModel() {
    var DEMO_MODE: Boolean = false

    /** USER MODEL AREA, HOLDS THE TOKEN, USER ID, AND TIME CREATED, etc **/
    var token: String? = null
    var createdDate: String? = null
    var userId: String? = null
    var username: String? = null
    var email: String? = null
    var emailVerified: Boolean = false

    fun populateFromLoginResponse(result: LoginApiService.ApiLoginResult){
        this.token = result.token
        this.createdDate = result.created
        this.userId = result.userId
    }
    fun populateFromUserResponse(result: UserApiService.ApiUserResult){
        this.username = result.username
        this.email = result.email
        this.emailVerified = result.emailVerified
    }

    //Home Todo Observables
    fun getActiveTodoos(appDatabase: AppDatabase): Flowable<List<TodoItemModel>>{
        return appDatabase.ToDoosDao().getAllActiveToDoos()
    }

    fun getCompletedTodoos(appDatabase: AppDatabase): Flowable<List<TodoItemModel>>{
        return appDatabase.ToDoosDao().getAllCompleted()
    }

    //Destroy ViewModel Data
    fun destroyViewModel(){
        DEMO_MODE = false
        token = null
        createdDate = null
        userId = null
        username = null
        email = null
        emailVerified = false
    }
}