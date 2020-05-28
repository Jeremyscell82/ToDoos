package com.lloydsbyte.todoos.database

import androidx.room.*
import com.lloydsbyte.todoos.home.TodoItemModel
import com.lloydsbyte.todoos.database.DB_Constant.Companion.dbVersion
import io.reactivex.Flowable

class DB_Constant {
    companion object {
        const val dbVersion = 1
    }
}


@Dao
interface ToDoosDao {

    //Get all todos that are completed
    @Query("SELECT * FROM todoitemmodel WHERE completed LIKE 'true'")
    fun getAllCompleted(): Flowable<List<TodoItemModel>>

    @Query("SELECT * FROM todoitemmodel WHERE completed LIKE 'false'")
    fun getAllActiveToDoos(): Flowable<List<TodoItemModel>>

    @Insert
    fun addTodo(vararg todoItemModel: TodoItemModel)

    @Delete
    fun deleteTodo(vararg todoItemModel: TodoItemModel)

    @Update
    fun updateTodo(vararg todoItemModel: TodoItemModel)

}

@Database(entities = [TodoItemModel::class], version = dbVersion, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ToDoosDao(): ToDoosDao
}