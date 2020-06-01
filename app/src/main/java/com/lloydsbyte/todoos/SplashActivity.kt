package com.lloydsbyte.todoos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.lloydsbyte.todoos.utilz.nav.NavController
import timber.log.Timber

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.d("JL_ Splash has started....")
        Handler().postDelayed({
            launchApp()
        }, 1000)
    }

    fun launchLoginFragment() {
        Timber.d("JL_ launching login....")
        NavController(supportFragmentManager.beginTransaction())
            .launchFragment(fragId = NavController.NavAction.LOGIN)
    }

    fun launchApp() {
        Timber.d("JL_ launching MainActivity....")
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
        finish()
    }

}