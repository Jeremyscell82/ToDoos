package com.lloydsbyte.todoos.login

import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.lloydsbyte.todoos.MainActivity
import com.lloydsbyte.todoos.MainViewModel
import com.lloydsbyte.todoos.R
import com.lloydsbyte.todoos.nav.NavController
import com.lloydsbyte.todoos.network.LoginApiService
import com.lloydsbyte.todoos.network.NetworkConstants
import com.lloydsbyte.todoos.utilz.RevealViewAnimator
import com.lloydsbyte.todoos.utilz.SharedPref_Controller
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.card_ip_config.*
import kotlinx.android.synthetic.main.card_ip_config.view.*
import kotlinx.android.synthetic.main.card_login.*
import kotlinx.android.synthetic.main.card_login.login_button
import kotlinx.android.synthetic.main.card_login.login_email
import kotlinx.android.synthetic.main.card_login.login_error
import kotlinx.android.synthetic.main.card_login.login_flip_fab
import kotlinx.android.synthetic.main.card_login.login_password
import kotlinx.android.synthetic.main.card_login.login_remember_device
import kotlinx.android.synthetic.main.card_login.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import timber.log.Timber

class LoginFragment : Fragment() {


    var loginDisposable: Disposable? = null
    val loginApiService by lazy {
        LoginApiService.ApiService.createLoginService(requireContext())
    }

    var userDisposable: Disposable? = null
    val userApiService by lazy {
        UserApiService.ApiService.createUserService(requireContext())
    }

    lateinit var loginViewModel: LoginViewModel
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mainViewModel = (activity as MainActivity).viewModel
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            //For quick entry in development
            login_ip_title.setOnLongClickListener {
                login_ip_address.setText("75.70.179.231")
                login_ip_port.setText("8282")
                //TODO JL_ DELETE
                login_email.setText("jeremysdev82@gmail.com")
                login_password.setText("0000")
                loginViewModel.address = login_ip_address.text.toString()
                Timber.d("JL_ ${loginViewModel.address}")
                loginViewModel.port = login_ip_port.text.toString()
                loginViewModel.email = login_email.text.toString()
                loginViewModel.password = login_password.text.toString()
                false
            }

            //IP Config Screen
            login_ip_save_button.setOnClickListener {
                saveServerAddress()
            }

            login_flip_fab.setOnClickListener {
                login_flip_view.flipTheView()
            }

            //Login Screen
            login_button.setOnClickListener {
                loginUser()
            }
            demo_button.setOnClickListener {
                showDemoModeWarning()
            }
            create_button.setOnClickListener {
                val bottomSheet = CreateAcctBottomSheet()
                bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
            }

            login_flip_view.visibility = View.INVISIBLE
            Handler().postDelayed({
                if (savedInstanceState == null && login_flip_view != null) RevealViewAnimator.revealView(
                    login_flip_view,
                    login_flip_view.width / 2,
                    login_flip_view.height / 2,
                    600L
                ) else {
                    login_flip_view.visibility = View.VISIBLE
                }
            }, 600)

            //Restore from shared preferences on first run only
            if (savedInstanceState == null) {
                val storedAddress = SharedPref_Controller(requireContext()).getServerAddress().split(":")
                if (storedAddress.size > 1) {
                    //Load up the viewmodel, onResume sets the view's texts
                    loginViewModel.address = storedAddress[0]
                    loginViewModel.port = storedAddress[1]
                    login_ip_save_button.setText(R.string.login_ip_continue)
                }
                val deviceId = SharedPref_Controller(requireContext()).getDeviceId().split(":")
                if (deviceId.size > 1) {
                    //Load up the viewmode, onResume sets the view's texts
                    loginViewModel.email = deviceId[0]
                    loginViewModel.password = deviceId[1]
                    login_remember_device.isChecked = true
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        Timber.d("JL_ on resume running")
        create_button.visibility =
            if (loginViewModel.address.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        login_ip_address.setText(loginViewModel.address)
        login_ip_port.setText(loginViewModel.port)
        login_email.setText(loginViewModel.email)
        login_password.setText(loginViewModel.password)
    }

    override fun onPause() {
        Timber.d("JL_ onPause ${login_ip_address.text.toString()}")
        super.onPause()
        loginDisposable?.dispose()
        userDisposable?.dispose()
        loginViewModel.address = login_ip_address.text.toString()
        Timber.d("JL_ ${loginViewModel.address}")
        loginViewModel.port = login_ip_port.text.toString()
        loginViewModel.email = login_email.text.toString()
        loginViewModel.password = login_password.text.toString()
    }


    private fun saveServerAddress() {
        loginViewModel.address = login_ip_address.text.toString()
        loginViewModel.port = login_ip_port.text.toString()
        if (Patterns.WEB_URL.matcher(loginViewModel.address).matches()) {
            Timber.d("JL_ address was valid")
            login_ip_error.visibility = View.GONE
            create_button.visibility = View.VISIBLE
            SharedPref_Controller(requireContext()).saveServerAddress("${loginViewModel.address}:${loginViewModel.port}")
            login_flip_view.flipTheView()
        } else {
            login_ip_error.visibility = View.VISIBLE
        }
    }

    private fun showDemoModeWarning() {
        MaterialDialog(requireActivity()).show {
            title(R.string.demo_mode_title)
            message(R.string.demo_mode_message)
            positiveButton(R.string.dialog_ok) { dialog ->
                dialog.dismiss()
                launchApp(true)
            }
            negativeButton(R.string.dialog_cancel)
        }
    }

    private fun loginUser() {
        loginViewModel.email = login_email.text.toString()
        loginViewModel.password = login_password.text.toString()
        if (loginViewModel.email.isNullOrEmpty() || loginViewModel.password.isNullOrEmpty()) {
            showErrorMessage(resources.getString(R.string.error_empty_fields))
        } else if (Patterns.EMAIL_ADDRESS.matcher(loginViewModel.email).matches()) {
            //Email is valid, submit to login
            connecting(true)
            loginDisposable = loginApiService.loginUser(
                mapOf(
                    "email" to loginViewModel.email,
                    "password" to loginViewModel.password
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        Timber.d("JL_ login success.... ")
                        result.let {
                            mainViewModel.populateFromLoginResponse(result = it!!)
                            getUserInfo(it.userId)
                        }


                    },
                    { error ->
                        Timber.d("JL_ login failed ${error.message}")
                        connecting(false)
                        showErrorMessage(resources.getString(R.string.error_account_login_fail))
                    }
                )
        } else {
            //Email is not valid
            showErrorMessage(resources.getString(R.string.error_invalid_email))
        }
    }

    private fun getUserInfo(userId: String) {
        //Save user credentials if 'Remember Device' is checked
        rememberDevice(wipe = !login_remember_device.isChecked)
        userDisposable = userApiService.getUser(
            token = mainViewModel.token!!,
            endpoint = NetworkConstants.usersEndpoint + userId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("JL_ login success.... ")
                    result.let {
                        mainViewModel.populateFromUserResponse(result = it!!)
                    }
                    connecting(false)
                    if (result != null) launchApp(false)
                },
                { error ->
                    Timber.d("JL_ Get user info failed ${error.message} $userId")
                    connecting(false)
                    showErrorMessage(resources.getString(R.string.error_user_api_fail))
                }
            )
    }

    private fun rememberDevice(wipe: Boolean) {
        val deviceId = if (wipe) "" else "${login_email.text.toString()}:${login_password.text.toString()}"
        SharedPref_Controller(requireContext()).rememberDevice(deviceId)
    }

    private fun launchApp(demoMode: Boolean) {
        mainViewModel.DEMO_MODE = demoMode
        closeLoginCard()
        (requireActivity() as MainActivity).navigateTo(NavController.NavAction.HOME)
    }

    private fun closeLoginCard() {
        RevealViewAnimator.hideView(
            login_flip_view,
            login_flip_view.width / 2,
            login_flip_view.height / 2,
            400L
        )
    }

    //Used for slow internet connections...
    private fun connecting(connecting: Boolean) {
        connecting_spinner.visibility = if (connecting) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(overrideMsg: String) {
        var message = resources.getString(R.string.login_error)
        if (!overrideMsg.isNullOrEmpty()) message = overrideMsg
        login_error.text = message
        login_error.visibility = View.VISIBLE
    }


}