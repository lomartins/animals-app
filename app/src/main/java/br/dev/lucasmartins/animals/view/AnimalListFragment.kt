package br.dev.lucasmartins.animals.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import br.dev.lucasmartins.animals.R
import br.dev.lucasmartins.animals.model.Animal
import br.dev.lucasmartins.animals.viewmodel.AnimalListViewModel
import kotlinx.android.synthetic.main.fragment_list.animalList
import kotlinx.android.synthetic.main.fragment_list.listError
import kotlinx.android.synthetic.main.fragment_list.loadingView
import kotlinx.android.synthetic.main.fragment_list.refreshLayout

class AnimalListFragment : Fragment() {

    private lateinit var viewModelAnimal: AnimalListViewModel
    private val listAdapter = AnimalListAdapter(arrayListOf())

    private val animalListDataObserver =  Observer<List<Animal>> { list ->
        list?.let {
            animalList.visibility = View.VISIBLE
            listAdapter.updateAnimalList(it)
        }
    }

    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        loadingView.visibility = if(isLoading) View.VISIBLE else View.GONE
        if(isLoading) {
            listError.visibility = View.GONE
            animalList.visibility = View.GONE
        }
    }

    private val errorLiveDataObserver = Observer<Boolean> { isError ->
        listError.visibility = if(isError) View.VISIBLE else View.GONE
        if(isError) {
            loadingView.visibility = View.GONE
            animalList.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelAnimal = ViewModelProvider(this).get(AnimalListViewModel::class.java).apply {
            animals.observe(viewLifecycleOwner, animalListDataObserver)
            loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
            loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
            refresh()
        }

        animalList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
        }

        refreshLayout.setOnRefreshListener {
            animalList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModelAnimal.hardRefresh()
            refreshLayout.isRefreshing = false
        }
    }

}