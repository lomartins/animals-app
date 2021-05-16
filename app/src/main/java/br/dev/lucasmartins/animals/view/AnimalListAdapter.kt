package br.dev.lucasmartins.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import br.dev.lucasmartins.animals.R
import br.dev.lucasmartins.animals.databinding.ItemAnimalBinding
import br.dev.lucasmartins.animals.model.Animal

class AnimalListAdapter(private val animalList: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>(), AnimalClickListener {

    fun updateAnimalList(newAnimalList: List<Animal>) {
        animalList.clear()
        animalList.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemAnimalBinding>(inflater, R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.view.animal = animalList[position]
        holder.view.listener = this
    }

    override fun getItemCount(): Int = animalList.size

    class AnimalViewHolder(var view: ItemAnimalBinding) : RecyclerView.ViewHolder(view.root)

    override fun onClick(v: View, animal: Animal) {
        val action = AnimalListFragmentDirections.actionDetail(animal)
        Navigation.findNavController(v).navigate(action)
    }
}