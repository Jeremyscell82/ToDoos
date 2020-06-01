package com.lloydsbyte.todoos.login

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.todoos.R
import com.lloydsbyte.todoos.utilz.network.CreateAcctApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottomsheet_create_acct.*
import kotlinx.android.synthetic.main.bottomsheet_create_acct.view.*
import timber.log.Timber


class CreateAcctBottomSheet: BottomSheetDialogFragment() {

    var creationDisposable: Disposable? = null
    val createAccountService by lazy {
        CreateAcctApiService.ApiService.create(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_create_acct, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            todoo_close_fab.setOnClickListener {
                dismiss()
            }
            create_account_button.setOnClickListener {
                if (validateFields()){
                    makeApiCall()
                }
            }
        }
    }

    private fun validateFields(): Boolean{
        //Get new values for fields
        val username = create_username.text.toString()
        val email = create_email.text.toString()
        val password = create_password.text.toString()
        val passwordConfirm = create_confirm_password.text.toString()
        //Check for missing fields
        return if (username.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || passwordConfirm.isNullOrEmpty()) {
            showErrorMessage(resources.getString(R.string.error_empty_fields))
            false
            //Check that passwords are the same
        } else if (password != passwordConfirm){
            showErrorMessage(resources.getString(R.string.error_passwords_mismatch))
            false
        } else {
            true
        }
    }

    private fun connecting(connecting: Boolean){
        create_loading_progress.visibility = if (connecting) View.VISIBLE else View.GONE
    }
    private fun showErrorMessage(message: String){
        create_acct_error.text = message
        create_acct_error.visibility = View.VISIBLE
        create_acct_title.visibility = View.GONE
    }


    private fun makeApiCall(){
        //Get values
        val username = create_username.text.toString()
        val email = create_email.text.toString()
        val password = create_password.text.toString()
        connecting(true)
        dialog?.setCancelable(false)
        creationDisposable = createAccountService.createUser(
            CreateAcctApiService.ApiModel(
                realm = "string",
                userName = username,
                email = email,
                emailVerified = false,
                password = password
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    dialog?.setCancelable(true)
                    Timber.d("JL_ success: ${result.userId}")
                    connecting(false)
                    Toast.makeText(requireContext(), resources.getString(R.string.toast_account_created), Toast.LENGTH_LONG).show()
                    dismissDialog()
                },
                { error ->
                    dialog?.setCancelable(true)
                    val errorMsg = error.message?:""
                    if (errorMsg.contains("HTTP 422")){
                        //This account already exist error
                        showErrorMessage(resources.getString(R.string.error_account_exist))
                    }
                    Timber.d("JL_ error: ${error.message}")
                }
            )
    }

    private fun dismissDialog(){
        this.dialog?.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        creationDisposable?.dispose()
    }


}
