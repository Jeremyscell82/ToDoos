package com.lloydsbyte.todoos.utilz.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Used in conjunction with RoomDB

@Entity
data class TodooModel(
    @PrimaryKey(autoGenerate = true)
    var dbKey: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "completed")
    var completed: Boolean,
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "id")
    var cloudId: String
)