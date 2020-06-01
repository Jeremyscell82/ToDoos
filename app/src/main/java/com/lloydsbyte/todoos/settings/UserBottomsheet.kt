package com.lloydsbyte.todoos.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lloydsbyte.todoos.R
import com.lloydsbyte.todoos.login.LoginViewModel
import kotlinx.android.synthetic.main.bottomsheet_todo.view.*

class UserBottomsheet: BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_create_acct, container, false)
    }
}