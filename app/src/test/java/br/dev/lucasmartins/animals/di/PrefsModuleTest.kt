package br.dev.lucasmartins.animals.di

import android.app.Application
import br.dev.lucasmartins.animals.util.SharedPreferencesHelper
import org.junit.Assert.*

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper): PrefsModule() {
    override fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}