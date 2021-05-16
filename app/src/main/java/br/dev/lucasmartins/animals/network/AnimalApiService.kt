package br.dev.lucasmartins.animals.network

import br.dev.lucasmartins.animals.di.DaggerApiComponent
import br.dev.lucasmartins.animals.model.Animal
import br.dev.lucasmartins.animals.model.ApiKeyResponse
import io.reactivex.Single
import javax.inject.Inject

class AnimalApiService {

    @Inject
    lateinit var api : AnimalApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getApiKey(): Single<ApiKeyResponse> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimals(key)
    }



}