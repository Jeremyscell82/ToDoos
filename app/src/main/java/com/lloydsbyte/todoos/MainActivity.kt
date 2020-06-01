package com.lloydsbyte.todoos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.lloydsbyte.todoos.utilz.database.AppDatabase
import com.lloydsbyte.todoos.utilz.database.TodooModel
import com.lloydsbyte.todoos.utilz.nav.NavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var appDatabase: AppDatabase
    var doubleBackQuit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "com.lloydsbyte.todoos.db"
        ).build()
        Timber.d("JL_ main activity running")
        if (savedInstanceState == null){
            navigateTo(NavController.NavAction.LOGIN)
        }

    }

    //Navigates fragments only
    fun navigateTo(fragId: NavController.NavAction) {
        NavController(supportFragmentManager.beginTransaction())
            .launchFragment(fragId = fragId)
    }

    fun logout(){
        //Todo log out api
        viewModel.destroyViewModel()
        navigateTo(NavController.NavAction.LOGIN)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 0 || doubleBackQuit) {
            super.onBackPressed()
        } else {
            doubleBackQuit = true
            Handler().postDelayed({ doubleBackQuit = false }, 2000)
            Snackbar.make(root_view, "Are you sure you want to leave?", Snackbar.LENGTH_LONG).setAction("Quit", View.OnClickListener { super.onBackPressed() }).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyViewModel()
    }

    /**
     * DATABASE CONTROLLER, COULD BE MOVED TO A BETTER LOCATION IM SURE BUT WHY NOT
     */
    fun saveTodoo(todooModel: TodooModel, newTodoo: Boolean){
        GlobalScope.launch {
            if (newTodoo){
                appDatabase.ToDoosDao().addTodo(todooModel)
            } else {
                appDatabase.ToDoosDao().updateTodo(todooModel)
            }

        }
    }

    fun saveCloudPull(todoos: List<TodooModel>) {
        Timber.d("JL_ saving items from the cloud ${todoos.size}")
        GlobalScope.launch {
            //Clear database prior to adding all todoos
            appDatabase.clearAllTables()
            appDatabase.ToDoosDao().addAllTodoos(todoos)
        }
    }

}
