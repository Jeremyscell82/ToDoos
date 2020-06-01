package com.lloydsbyte.todoos.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.todoos.MainActivity
import com.lloydsbyte.todoos.R
import com.lloydsbyte.todoos.SplashActivity
import com.lloydsbyte.todoos.utilz.database.Converter
import com.lloydsbyte.todoos.utilz.database.TodooModel
import com.lloydsbyte.todoos.utilz.network.ToDoosApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottomsheet_todo.*
import timber.log.Timber

/**
 * When a todoo item is clicked or when adding a new todo, this bottomsheet will appear
 */
class TodooBottomsheet : BottomSheetDialogFragment() {


    lateinit var todooViewModel: TodooViewModel

    var todoServiceDisposable: Disposable? = null
    val saveTodooService by lazy {
        ToDoosApiService.ApiService.create(requireContext())
    }

    companion object {
        //Used only to pass in an existing todoo
        var existingTodoo: TodooModel? = null
        //For existing todoo's
        fun newInstance(todooModel: TodooModel?): TodooBottomsheet {
            Timber.d("JL_ new instance created ${todooModel?.cloudId}")
            existingTodoo = todooModel
            Timber.d("JL_ new instance created ${existingTodoo?.cloudId}")
            return TodooBottomsheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todooViewModel = ViewModelProvider(this).get(TodooViewModel::class.java)
        Timber.d("JL_ checking if the existing todo was null or not ${existingTodoo?.cloudId}")
        if (existingTodoo != null) {
            Timber.d("JL_ todoomodel was not null...")
            todooViewModel.todooModel = existingTodoo!!
        }
        return inflater.inflate(R.layout.bottomsheet_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoo_close_fab.setOnClickListener {
            dialog?.dismiss()
        }
        todoo_save_fab.setOnClickListener {
            if (validateFields()) {
                saveTodoo()
            }
        }
        todoo_completed_card_button.setOnClickListener {
            val curCompletedStatus = todooViewModel.todooModel?.completed ?: false
            setCompleted(!curCompletedStatus)
        }
        todoo_delete_card_button.setOnClickListener {
            //Todo delete dialog
        }
        setUI()
    }

    private fun setUI() {
        Timber.d("JL_ setting ui")
        todoo_title.setText(todooViewModel.todooModel.title)
        todoo_desc_text.setText(todooViewModel.todooModel.description)
        todoo_url_text.setText(todooViewModel.todooModel.url)
        setCompleted(todooViewModel.todooModel.completed)
        todoo_local_id.text =
            resources.getString(R.string.todoo_local_id, todooViewModel.todooModel.dbKey)
        todoo_cloud_id.text =
            resources.getString(R.string.todoo_cloud_id, todooViewModel.todooModel.cloudId)
    }


    fun validateFields(): Boolean {
        var validated = true
        if (!todoo_title.text.toString().isNullOrEmpty()) {
            todooViewModel.todooModel.title = todoo_title.text.toString()
        } else {
            validated = false
            todoo_error.setText(R.string.error_todoo_title)
        }

        if (!todoo_desc_text.text.toString().isNullOrEmpty())
            todooViewModel.todooModel?.description = todoo_desc_text.text.toString()

        val url = todoo_url_text.text.toString()
        if (!url.isNullOrEmpty()) {
            if (Patterns.WEB_URL.matcher(url).matches()) {
                todooViewModel.todooModel?.url = url
            } else {
                todoo_url_layout.error = "Invalid URL"
            }
        }
        return validated
    }

    private fun setCompleted(completed: Boolean) {
        Timber.d("JL_ setting completed as $completed")
        if (completed) {
            todoo_completed_fab.show()
            todooViewModel.todooModel?.completed = true
        } else {
            todoo_completed_fab.hide()
            todooViewModel.todooModel?.completed = false
        }
        Timber.d("JL_ completed is now ${todooViewModel.todooModel.completed}")
    }

    private fun saveTodoo() {
        dialog?.setCancelable(false)
        val userId = (requireActivity() as MainActivity).viewModel.userId
        val token = (requireActivity() as MainActivity).viewModel.token
        if (userId == null || token == null) {
            criticalError()
        } else {
            todoServiceDisposable = dynamicApiModel(token, userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        dialog?.setCancelable(true)
                        Toast.makeText(
                            requireContext(),
                            "${todooViewModel.todooModel.title} saved",
                            Toast.LENGTH_LONG
                        ).show()
                        (requireActivity() as MainActivity).saveTodoo(
                            Converter.ConvertToDooItem(result as ToDoosApiService.ApiResult), existingTodoo == null
                        )
                        dialog?.dismiss()
                    },
                    { error ->
                        dialog?.setCancelable(true)
                        Timber.d("JL_ error: ${error.message}")
                    }
                )
        }
    }

    //Only able to do this as the result is not to importance
    private fun dynamicApiModel(token: String, userId: String): Observable<*> {
        return if (existingTodoo == null) {
            //This is a new todoo, create with out cloud id
            saveTodooService.postNewTodo(
                token,
                ToDoosApiService.ApiModelWoId(
                    userId = userId,
                    title = todooViewModel.todooModel.title,
                    description = todooViewModel.todooModel.description,
                    completed = todooViewModel.todooModel.completed,
                    url = todooViewModel.todooModel.url
                )
            )
        } else {
            saveTodooService.postTodo(
                token,
                ToDoosApiService.ApiResult(
                    cloudId = todooViewModel.todooModel.cloudId,
                    userId = userId,
                    title = todooViewModel.todooModel.title,
                    description = todooViewModel.todooModel.description,
                    completed = todooViewModel.todooModel.completed,
                    url = todooViewModel.todooModel.url
                )
            )
        }
    }

    private fun criticalError() {
        val mainActivity = (requireActivity() as MainActivity)
        Toast.makeText(
            mainActivity,
            "Something has gone wrong, please sign in again",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(mainActivity, SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        mainActivity.overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
        mainActivity.finish()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        todoServiceDisposable?.dispose()
    }
}