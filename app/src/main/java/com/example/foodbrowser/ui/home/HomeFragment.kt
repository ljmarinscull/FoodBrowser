package com.example.foodbrowser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodbrowser.R
import com.example.foodbrowser.data.remote.RetrofitClient
import com.example.foodbrowser.databinding.FragmentHomeBinding
import com.example.foodbrowser.domain.models.Food
import com.example.foodbrowser.domain.repository.FoodRepository
import com.example.foodbrowser.utils.Resource
import com.example.foodbrowser.utils.afterTextChanged
import com.example.foodbrowser.utils.mappers.FoodMapper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var _adapter: FoodAdapter
    private val _viewModel: FoodViewModel by activityViewModels<FoodViewModel>(
        factoryProducer = {
            FoodViewModelFactory(
                repo = FoodRepository(
                    api = RetrofitClient.getInstance(),
                    foodMapper = FoodMapper
                )
            )
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        initRecyclerView()
        observer()
    }

    private fun observer() {
        _viewModel.foods.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _adapter.addItems(it.data)
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), R.string.get_foods_error, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.foods_menu, menu)
                val menuItem = menu.findItem(R.id.action_search)
                val searchView = menuItem.actionView as SearchView?
                searchView?.afterTextChanged {
                    _viewModel.searchFoodByName(it)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initRecyclerView() = with(binding.recyclerView) {
        addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        layoutManager = LinearLayoutManager(requireContext())
        _adapter = FoodAdapter(::onClickListener)
        adapter = _adapter
    }

    private fun onClickListener(food: Food) {
        Toast.makeText(requireContext(), food.name, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }
}