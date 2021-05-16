package br.dev.lucasmartins.animals.view

import android.view.View
import br.dev.lucasmartins.animals.model.Animal

interface AnimalClickListener {
    fun onClick(v: View, animal: Animal)
}