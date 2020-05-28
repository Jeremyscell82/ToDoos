package com.lloydsbyte.todoos.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lloydsbyte.todoos.MainActivity
import com.lloydsbyte.todoos.MainViewModel
import com.lloydsbyte.todoos.R
import com.lloydsbyte.todoos.database.Converter
import com.lloydsbyte.todoos.network.ToDoosApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.home_recyclerview
import timber.log.Timber

class HomeFragment: Fragment() {

    var todoDisposable: Disposable? = null
    val todooApiService by lazy {
        ToDoosApiService.ApiService.create(requireContext())
    }

    var todoDbDisposable: Disposable? = null

    lateinit var homeAdapter: HomeAdapter
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeAdapter = HomeAdapter()
        mainViewModel = (activity as MainActivity).viewModel
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            open_menu.setOnClickListener {
                open_menu.isExpanded = true
            }
            close_menu.setOnClickListener {
                open_menu.isExpanded = false
            }
            home_recyclerview.apply {
                layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
                adapter = homeAdapter
            }
        }
        todoDbDisposable = mainViewModel.getActiveTodoos((requireActivity() as MainActivity).appDatabase)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("New entries have been added, reload the list with ${result.size} items")
                    homeAdapter.updateAdapter(articles = result, recyclerView = home_recyclerview, animate = true)
                },
                { error ->
                    Timber.d("Something has gone wrong $error")
                }
            )

        if (savedInstanceState == null)
            refreshData()
    }

    fun refreshData(){
        todoDisposable = todooApiService.getAllTodoos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("JL_ login success.... ")
                    val todoos = Converter.ConvertToDooItem(result)
                },
                { error ->

                }
            )
    }

}