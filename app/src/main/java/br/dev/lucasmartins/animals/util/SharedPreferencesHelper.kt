package br.dev.lucasmartins.animals.util

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(private val context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun saveApiKey(key: String?) {
        prefs.edit().putString(PREF_API_KEY, key).apply()
    }

    fun getApiKey() = prefs.getString(PREF_API_KEY, null)

    companion object {
        private const val PREF_API_KEY = "API_KEY"
    }
}