package com.lloydsbyte.todoos

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lloydsbyte.todoos.utilz.RevealViewAnimator
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment: Fragment() {

    val emailRegEx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            login_button.setOnClickListener {

            }
            login_card.visibility = View.INVISIBLE
            Handler().postDelayed({
                if (savedInstanceState == null && login_card != null) RevealViewAnimator.revealView(
                    login_card,
                    login_card.width / 2,
                    login_card.height / 2,
                    600L
                )
            }, 600)
        }
    }

    private fun loginUser(){
        val email = login_email.text.toString()
        val pass = login_password.text.toString()
        if (email.isNullOrEmpty() || pass.isNullOrEmpty()) {
            login_error.visibility = View.VISIBLE
            login_error.text = resources.getString(R.string.login_error)
        } else if (email.matches(Regex(emailRegEx))) {
            //Email is valid, submit to login
            showConnectionView()
        }
    }

    private fun showConnectionView(){
        RevealViewAnimator.revealView(login_card,
            login_card.width / 2,
            login_card.height / 2,
            400L)
        connecting_view.visibility = View.VISIBLE
    }
}