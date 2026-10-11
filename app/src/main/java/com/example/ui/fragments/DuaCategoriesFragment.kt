package com.example.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.R
import com.example.databinding.FragmentDuaCategoriesBinding
import com.example.databinding.FragmentDuaCategoriesBinding.inflate
import com.example.model.DuaCategory
import com.example.ui.adapters.DuaCategoriesAdapter
import com.example.ui.navigation.navigateWithSlide
import com.example.viewmodel.SakinahViewModel
import kotlin.getValue

class DuaCategoriesFragment : BaseFragment<FragmentDuaCategoriesBinding>(FragmentDuaCategoriesBinding::inflate) {

    private val viewModel: SakinahViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDuaCategoriesBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = DuaCategoriesAdapter(DuaCategory.CATEGORIES) { selected ->
            viewModel.selectDuaCategory(selected)
            findNavController().navigateWithSlide(R.id.duaListFragment)
        }
        binding.rvDuaCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDuaCategories.adapter = adapter
    }
}
