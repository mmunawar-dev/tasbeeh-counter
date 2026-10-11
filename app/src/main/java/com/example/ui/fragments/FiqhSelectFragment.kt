package com.example.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentFiqhSelectBinding
import com.example.databinding.ItemCalculationMethodOptionBinding
import com.example.databinding.ItemMadhabOptionBinding
import com.example.model.CalculationMethod
import com.example.model.Madhab
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FiqhSelectFragment : BaseFragment<FragmentFiqhSelectBinding>(FragmentFiqhSelectBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnFiqhBack.visibility = View.VISIBLE
    binding.btnFiqhBack.setOnClickListener {
      findNavController().navigateUp()
    }

    binding.btnFiqhApply.setOnClickListener {
      findNavController().navigateUp()
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.collectLatest { state ->
        renderMadhabs(state.selectedMadhab)
        renderMethods(state.calculationMethod)
      }
    }
  }

  private fun renderMadhabs(selectedMadhab: Madhab) {
    binding.llMadhabContainer.removeAllViews()
    val inflater = LayoutInflater.from(requireContext())

    Madhab.entries.forEach { madhab ->
      val itemBinding = ItemMadhabOptionBinding.inflate(inflater, binding.llMadhabContainer, false)
      val isSelected = madhab == selectedMadhab

      itemBinding.tvMadhabName.text = madhab.displayName
      itemBinding.tvMadhabSubtitle.text = madhab.subtitle

      if (madhab == Madhab.HANAFI) {
        itemBinding.tvMadhabRecommendedBadge.visibility = View.VISIBLE
      } else {
        itemBinding.tvMadhabRecommendedBadge.visibility = View.GONE
      }

      val context = requireContext()
      if (isSelected) {
        itemBinding.cardMadhab.setCardBackgroundColor(ContextCompat.getColor(context, R.color.active_prayer_bg))
        itemBinding.cardMadhab.strokeColor = ContextCompat.getColor(context, R.color.active_prayer_border)
        itemBinding.cardMadhab.strokeWidth = (1.5f * resources.displayMetrics.density).toInt()
        itemBinding.layoutMadhabCheckCircle.backgroundTintList = ColorStateList.valueOf(
          ContextCompat.getColor(context, R.color.emerald_primary)
        )
        itemBinding.ivMadhabCheckIcon.visibility = View.VISIBLE
      } else {
        itemBinding.cardMadhab.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_light))
        itemBinding.cardMadhab.strokeColor = ContextCompat.getColor(context, R.color.outline_border)
        itemBinding.cardMadhab.strokeWidth = (1f * resources.displayMetrics.density).toInt()
        itemBinding.layoutMadhabCheckCircle.backgroundTintList = ColorStateList.valueOf(
          ContextCompat.getColor(context, R.color.surface_variant_light)
        )
        itemBinding.ivMadhabCheckIcon.visibility = View.GONE
      }

      itemBinding.cardMadhab.setOnClickListener {
        viewModel.selectMadhab(madhab)
      }

      binding.llMadhabContainer.addView(itemBinding.root)
    }
  }

  private fun renderMethods(selectedMethod: CalculationMethod) {
    binding.llMethodsContainer.removeAllViews()
    val inflater = LayoutInflater.from(requireContext())

    CalculationMethod.entries.forEach { method ->
      val itemBinding = ItemCalculationMethodOptionBinding.inflate(inflater, binding.llMethodsContainer, false)
      val isSelected = method == selectedMethod

      itemBinding.tvMethodName.text = method.displayName
      itemBinding.tvMethodRegion.text = "Used in: ${method.recommendedRegion}"

      val context = requireContext()
      if (isSelected) {
        itemBinding.cardMethod.setCardBackgroundColor(ContextCompat.getColor(context, R.color.active_prayer_bg))
        itemBinding.cardMethod.strokeColor = ContextCompat.getColor(context, R.color.active_prayer_border)
        itemBinding.cardMethod.strokeWidth = (1.5f * resources.displayMetrics.density).toInt()
        itemBinding.ivMethodCheck.visibility = View.VISIBLE
      } else {
        itemBinding.cardMethod.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_light))
        itemBinding.cardMethod.strokeColor = ContextCompat.getColor(context, R.color.outline_border)
        itemBinding.cardMethod.strokeWidth = (1f * resources.displayMetrics.density).toInt()
        itemBinding.ivMethodCheck.visibility = View.GONE
      }

      itemBinding.cardMethod.setOnClickListener {
        viewModel.selectCalculationMethod(method)
      }

      binding.llMethodsContainer.addView(itemBinding.root)
    }
  }
}
