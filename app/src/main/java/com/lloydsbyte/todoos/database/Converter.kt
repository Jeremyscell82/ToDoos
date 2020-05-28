package com.lloydsbyte.todoos.database

import com.lloydsbyte.todoos.home.TodoItemModel
import com.lloydsbyte.todoos.network.ToDoosApiService

class Converter {
    companion object {
        fun ConvertToDooItem(resultItems: List<ToDoosApiService.ApiResult>): List<TodoItemModel> {
            return resultItems.map {
                TodoItemModel(
                    dbKey = 0,
                    title = it.title,
                    description = it.description,
                    completed = it.completed,
                    url = it.url,
                    cloudId = it.cloudId
                )
            }
        }
    }
}