package com.lloydsbyte.todoos.utilz

import com.lloydsbyte.todoos.home.TodoItemModel

class CompareModels {
    companion object {
        fun compareTodoos(list1: List<TodoItemModel>, list2: List<TodoItemModel>): Boolean {
            if (list1.size != list2.size)
                return false

            val pairList = list1.zip(list2)

            return pairList.all { (elt1, elt2) ->
                elt1 != elt2
            }
        }
    }
}