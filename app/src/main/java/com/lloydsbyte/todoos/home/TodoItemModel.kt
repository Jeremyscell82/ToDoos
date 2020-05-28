package com.lloydsbyte.todoos.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Used in conjunction with RoomDB

@Entity
data class TodoItemModel(
    @PrimaryKey(autoGenerate = true)
    val dbKey: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "completed")
    val completed: Boolean,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "id")
    val cloudId: String
)