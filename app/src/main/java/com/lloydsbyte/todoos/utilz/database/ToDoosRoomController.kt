package com.lloydsbyte.todoos.utilz.database

import androidx.room.*
import com.lloydsbyte.todoos.utilz.database.DB_Constant.Companion.dbVersion
import io.reactivex.Flowable

class DB_Constant {
    companion object {
        const val dbVersion = 1
    }
}


@Dao
interface ToDoosDao {

    //Get all todos that are completed
    @Query("SELECT * FROM todoomodel WHERE completed LIKE 1")
    fun getAllCompleted(): Flowable<List<TodooModel>>

    @Query("SELECT * FROM todoomodel WHERE completed LIKE 0")
    fun getAllActiveToDoos(): Flowable<List<TodooModel>>

    @Query("SELECT * FROM todoomodel")
    fun getAllTodoos(): Flowable<List<TodooModel>>

    @Transaction
    fun addAllTodoos(todoos: List<TodooModel>) {
        for (todoo in todoos) {
            addTodo(todoo)
        }
    }

    @Insert
    fun addTodo(vararg todoItemModel: TodooModel)

    @Update
    fun updateTodo(vararg todoItemModel: TodooModel)

    @Delete
    fun deleteTodo(vararg todoItemModel: TodooModel)

}

@Database(entities = [TodooModel::class], version = dbVersion, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ToDoosDao(): ToDoosDao
}