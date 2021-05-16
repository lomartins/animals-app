package br.dev.lucasmartins.animals.di

import br.dev.lucasmartins.animals.network.AnimalApi
import br.dev.lucasmartins.animals.network.AnimalApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {

    @Provides
    fun provideAnimalApi(): AnimalApi {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }.build().create(AnimalApi::class.java)
    }

    @Provides
    open fun provideAnimalApiService(): AnimalApiService {
        return AnimalApiService()
    }

    companion object {
        private const val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net/"
    }
}