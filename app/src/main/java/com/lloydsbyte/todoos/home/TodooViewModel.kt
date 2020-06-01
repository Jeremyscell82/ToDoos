package com.lloydsbyte.todoos.home

import androidx.lifecycle.ViewModel
import com.lloydsbyte.todoos.utilz.database.TodooModel

class TodooViewModel: ViewModel() {

    var todooModel: TodooModel =
        TodooModel(0, "", "", false, "", "")
}