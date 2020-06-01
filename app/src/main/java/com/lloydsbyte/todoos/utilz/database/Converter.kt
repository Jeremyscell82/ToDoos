package com.lloydsbyte.todoos.utilz.database

import com.lloydsbyte.todoos.utilz.network.ToDoosApiService

class Converter {
    companion object {
        fun ConvertToDooItems(resultItems: List<ToDoosApiService.ApiResult>): List<TodooModel> {
            return resultItems.map {
                TodooModel(
                    dbKey = 0,
                    title = it.title,
                    description = it.description,
                    completed = it.completed,
                    url = it.url,
                    cloudId = it.cloudId
                )
            }
        }

        //Todo temp fun
        fun ConvertToDooItem(resultItem: ToDoosApiService.ApiResult): TodooModel {
            return TodooModel(
                dbKey = 0,
                title = resultItem.title,
                description = resultItem.description,
                completed = resultItem.completed,
                url = resultItem.url,
                cloudId = resultItem.cloudId
            )
        }
    }
}