package com.saal.androidtest.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout
import com.saal.androidtest.ui.MainActivity

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    abstract val viewModel: BaseViewModel

    protected lateinit var supportActionBar: ActionBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeActionBar()
        setupEditTextsBehaviour(binding.root as ViewGroup)

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupEditTextsBehaviour(root: ViewGroup) {
        root.children.forEach { childView ->
            when (childView) {
                is TextInputLayout -> {
                    childView.editText?.setOnEditorActionListener { view, actionId, _ ->
                        when (actionId) {
                            EditorInfo.IME_ACTION_DONE -> {
                                view.clearFocus()
                                (activity as MainActivity).closeKeyboard()
                                return@setOnEditorActionListener true
                            }

                            EditorInfo.IME_ACTION_NEXT -> {
                                val nextView = view.focusSearch(View.FOCUS_DOWN) as EditText
                                nextView.requestFocus()
                                nextView.setSelection(nextView.text.length)
                                return@setOnEditorActionListener true
                            }
                        }
                        return@setOnEditorActionListener false
                    }
                }

                is ViewGroup -> {
                    setupEditTextsBehaviour(childView)
                }
            }
        }
    }



    private fun initializeActionBar() {
        val activity = (activity as? AppCompatActivity)
        activity?.let {
            it.supportActionBar?.let { actionBar ->
                supportActionBar = actionBar
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): T
}