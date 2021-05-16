package br.dev.lucasmartins.animals.di

import br.dev.lucasmartins.animals.network.AnimalApiService

class ApiModuleTest(val mockService: AnimalApiService): ApiModule() {
    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}