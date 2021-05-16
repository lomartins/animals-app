package br.dev.lucasmartins.animals.di

import br.dev.lucasmartins.animals.network.AnimalApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: AnimalApiService)
}