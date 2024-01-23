package com.example.foodbrowser.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbrowser.R
import com.example.foodbrowser.databinding.FoodItemLayoutBinding
import com.example.foodbrowser.domain.models.Food
import java.util.Locale

class FoodAdapter(
    private val _listener: (Food) -> Unit
) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>(), Filterable {

    private lateinit var _context: Context
    var _itemsAll: List<Food> = listOf()

    private val _diffCallback = object : DiffUtil.ItemCallback<Food>() {

        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }
    private val _differ = AsyncListDiffer(this, _diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_item_layout, parent, false)

        _context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = _differ.currentList[position]
        holder.itemView.setOnClickListener {
            _listener(food)
        }
        holder.bind(food)
    }

    override fun getItemCount() = _differ.currentList.size

    fun addItems(items: List<Food>) {
        _itemsAll = items
        _differ.submitList(items)
    }

    private var _filter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<Food> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(_itemsAll)
            } else {
                filteredList.addAll(_itemsAll.filter {
                    it.name.lowercase(Locale.getDefault())
                        .contains(
                            charSequence.toString()
                                .lowercase(Locale.getDefault())
                        )
                })
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        @SuppressLint("NotifyDataSetChanged")
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            _differ.submitList(filterResults.values as List<Food>)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FoodItemLayoutBinding.bind(view)

        fun bind(food: Food) = with(binding) {
            nameLabel.text = food.name
        }
    }

    override fun getFilter() = _filter
}