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
import com.lloydsbyte.todoos.utilz.database.Converter
import com.lloydsbyte.todoos.utilz.nav.NavController
import com.lloydsbyte.todoos.utilz.network.ToDoosApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.home_recyclerview
import timber.log.Timber

class HomeFragment: Fragment() {

    var todooDisposable: Disposable? = null
    val todooApiService by lazy {
        ToDoosApiService.ApiService.create(requireContext())
    }

    var todooDbDisposable: Disposable? = null

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
            toolbar_title.text = mainViewModel.username
            open_menu.setOnClickListener {
                open_menu.isExpanded = true
            }
            close_menu.setOnClickListener {
                open_menu.isExpanded = false
            }
            home_recyclerview.apply {
                layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
                adapter = homeAdapter
                homeAdapter.onItemClicked = {
                    val bottomSheet = TodooBottomsheet.newInstance(it)
                    bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
                }
            }
            home_fab_add.setOnClickListener {
                val bottomSheet = TodooBottomsheet.newInstance(null)
                bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
                open_menu.isExpanded = false
            }
            home_fab_completed.setOnClickListener {
                open_menu.isExpanded = false
                switchTodoosToShow()
            }
            home_swiperefresh_layout.setOnRefreshListener {
                refreshData()
            }
            home_settings_fab.setOnClickListener {
                NavController(requireActivity().supportFragmentManager.beginTransaction()).launchFragment(NavController.NavAction.SETTINGS)
            }

        }

        todooDbDisposable = mainViewModel.getAllTodoos((requireActivity() as MainActivity).appDatabase)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("New entries have been added, reload the list with ${result.size} items")
                    mainViewModel.todooList = result
                    homeAdapter.updateAdapter(articles = result.filter { it.completed == mainViewModel.showingCompleted }, recyclerView = home_recyclerview, animate = true)
                },
                { error ->
                    Timber.d("Something has gone wrong $error")
                }
            )

        if (savedInstanceState == null)
            refreshData()
    }

    private fun switchTodoosToShow(){
        if (mainViewModel.showingCompleted){
            //Showing completed, switch to active list
            mainViewModel.showingCompleted = !mainViewModel.showingCompleted
            home_fab_completed.text = resources.getString(R.string.button_completed)
        } else {
            //Showing active list, switch to completed
            mainViewModel.showingCompleted = !mainViewModel.showingCompleted
            home_fab_completed.text = resources.getString(R.string.button_completed_active)
        }
        homeAdapter.updateAdapter(articles = mainViewModel.todooList.filter { it.completed == mainViewModel.showingCompleted }, recyclerView = home_recyclerview, animate = true)
    }

    fun refreshData(){
        todooDisposable = todooApiService.getAllTodoos(
            mainViewModel.token!!,
            mainViewModel.genTodoosEndpoint()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("JL_ login success.... ${result.size}")
                    val allTodoos = Converter.ConvertToDooItems(result)
                    (requireActivity() as MainActivity).saveCloudPull(allTodoos)
                    home_swiperefresh_layout.isRefreshing = false
                },
                { error ->
                    Timber.d("JL_ something has gone wrong getting the todoos $error")
                    home_swiperefresh_layout.isRefreshing = false
                }
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        todooDisposable?.dispose()
        todooDbDisposable?.dispose()
    }

}