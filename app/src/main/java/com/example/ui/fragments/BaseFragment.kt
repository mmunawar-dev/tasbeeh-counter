package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias BindingInflater<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB

/**
 * Reusable base Fragment handling ViewBinding lifecycle initialization and cleanup safely.
 * Subclasses obtain clean, non-null access to [binding] while the view is alive.
 */
abstract class BaseFragment<VB : ViewBinding>(
  private val bindingInflater: BindingInflater<VB>
) : Fragment() {

  private var _binding: VB? = null
  protected val binding: VB
    get() = _binding ?: throw IllegalStateException(
      "ViewBinding is only accessible between onCreateView and onDestroyView"
    )

  val isBindingAvailable: Boolean
    get() = _binding != null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = bindingInflater(inflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
