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

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
