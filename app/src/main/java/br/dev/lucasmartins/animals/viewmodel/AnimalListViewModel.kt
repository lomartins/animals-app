package br.dev.lucasmartins.animals.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.dev.lucasmartins.animals.di.ApiModule_ProvideAnimalApiFactory.create
import br.dev.lucasmartins.animals.di.AppModule
import br.dev.lucasmartins.animals.di.CONTEXT_APP
import br.dev.lucasmartins.animals.di.DaggerViewModelComponent
import br.dev.lucasmartins.animals.di.TypeOfContext
import br.dev.lucasmartins.animals.model.Animal
import br.dev.lucasmartins.animals.model.ApiKeyResponse
import br.dev.lucasmartins.animals.network.AnimalApiService
import br.dev.lucasmartins.animals.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AnimalListViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean): this(application) {
        injected = true
    }

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: AnimalApiService

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefs: SharedPreferencesHelper

    private var invalidApiKey: Boolean = false
    private var injected = false

    fun inject() {
        if(!injected) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    fun refresh() {
        loading.value = true
        invalidApiKey = false
        val key = prefs.getApiKey()
        if (key.isNullOrEmpty()) {
            getKey()
        } else {
            refreshAnimals(key)
        }
    }

    fun hardRefresh() {
        loading.value = true
        getKey()
    }

    private fun getKey() {
        disposable.add(
            apiService.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKeyResponse>() {
                    override fun onSuccess(keyResponse: ApiKeyResponse) {
                        if (keyResponse.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefs.saveApiKey(keyResponse.key)
                            refreshAnimals(keyResponse.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                    }

                })
        )
    }

    private fun refreshAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(animalResponse: List<Animal>) {
                        loading.value = false
                        if (animalResponse.isEmpty()) {
                            loadError.value = true
                        } else {
                            loadError.value = false
                            animals.value = animalResponse
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (invalidApiKey.not()) {
                            invalidApiKey = true
                            getKey()
                        } else {
                            e.printStackTrace()
                            loading.value = false
                            loadError.value = true
                            animals.value = null
                        }
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}