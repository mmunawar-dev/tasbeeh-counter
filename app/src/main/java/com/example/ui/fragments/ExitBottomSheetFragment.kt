package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.databinding.FragmentExitBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExitBottomSheetFragment(
  private val onConfirmExit: () -> Unit
) : BottomSheetDialogFragment() {

  private var _binding: FragmentExitBottomSheetBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentExitBottomSheetBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnExitCancel.setOnClickListener {
      dismiss()
    }

    binding.btnExitConfirm.setOnClickListener {
      dismiss()
      onConfirmExit()
    }
  }

  override fun onStart() {
    super.onStart()
    val bottomSheetDialog = dialog as? com.google.android.material.bottomsheet.BottomSheetDialog
    bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let { bottomSheet ->
      val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet)
      behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
      behavior.skipCollapsed = true
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
