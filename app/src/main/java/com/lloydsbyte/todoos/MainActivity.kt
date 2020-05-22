package com.lloydsbyte.todoos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lloydsbyte.todoos.nav.NavController

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        if (viewModel.token == null)
            navigateTo(NavController.NavAction.LOGIN)
    }

    //Navigates fragments only
    fun navigateTo(fragId: NavController.NavAction) {
        NavController(supportFragmentManager.beginTransaction())
            .launchFragment(fragId = fragId)
    }

    fun logout(){
        //Todo log out api
        viewModel.token = null
        navigateTo(NavController.NavAction.LOGIN)
    }
}
