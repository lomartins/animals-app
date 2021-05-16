package br.dev.lucasmartins.animals.di

import br.dev.lucasmartins.animals.viewmodel.AnimalListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefsModule::class, AppModule::class])
interface ViewModelComponent {

    fun inject(viewModel: AnimalListViewModel)
}