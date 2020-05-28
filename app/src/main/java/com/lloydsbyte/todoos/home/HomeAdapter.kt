package com.lloydsbyte.todoos.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.lloydsbyte.todoos.R
import com.lloydsbyte.todoos.utilz.CompareModels
import kotlinx.android.synthetic.main.item_todo.view.*
import timber.log.Timber

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var adapterItems: List<TodoItemModel> = emptyList()
    var onItemClicked: ((TodoItemModel) -> Unit)? = null

    fun updateAdapter(articles: List<TodoItemModel>, recyclerView: RecyclerView, animate: Boolean) {
        if (!CompareModels.compareTodoos(adapterItems, articles)) {
            Timber.d("JL_ updating the android adapter")
            adapterItems = articles
            if (animate) {
                val anim = AnimationUtils.loadLayoutAnimation(
                    recyclerView.context,
                    R.anim.recyclerview_home_anim
                )
                recyclerView.layoutAnimation = anim
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.scheduleLayoutAnimation()
            } else {
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(inflater.inflate(R.layout.item_todo, parent, false))
    }

    override fun getItemCount() = adapterItems.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(adapterItems[position])
    }

    inner class HomeViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(todo: TodoItemModel){
            itemView.apply {
                item_todo_card.setOnClickListener {
                    onItemClicked?.invoke(todo)
                }
                item_todo_title.text = todo.title
                item_todo_done_fab.setOnClickListener {

                }
            }
        }
    }
}