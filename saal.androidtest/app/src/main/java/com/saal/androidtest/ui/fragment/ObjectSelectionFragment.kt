package com.saal.androidtest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saal.androidtest.R
import com.saal.androidtest.databinding.FragmentObjectSelectionBinding
import com.saal.androidtest.ui.adapter.ObjectAdapter
import com.saal.androidtest.ui.base.BaseFragment
import com.saal.androidtest.ui.viewmodel.ObjectListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ObjectSelectionFragment : BaseFragment<FragmentObjectSelectionBinding>() {

    companion object {
        const val OBJECT_SELECTION_REQUEST = "OBJECT_SELECTION_REQUEST"
        const val SELECTED_OBJECT_KEY = "SELECTED_OBJECT_KEY"
    }

    override val viewModel by viewModel<ObjectListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchEt.doAfterTextChanged { text ->
            viewModel.filterEntries(text.toString())
        }

        viewModel.objectList.observe(viewLifecycleOwner) { objects ->
            binding.objectsRv.apply {
                layoutManager = LinearLayoutManager(context)

                val uiList = objects.map {
                    ObjectAdapter.ObjectListModel(
                        viewType = ObjectAdapter.ViewType.ObjectSelectionItem(),
                        data = it
                    )
                }.toMutableList()
                if(uiList.isEmpty()){
                    uiList.add(ObjectAdapter.ObjectListModel(
                        viewType = ObjectAdapter.ViewType.NoEntriesItem(R.string.objects),
                        data = null
                    ))
                }
                adapter = ObjectAdapter(
                    uiList,
                    onObjectClickedCallback = { clickedObject ->
                        // Set the result to be sent back to MainFragment
                        parentFragmentManager.setFragmentResult(OBJECT_SELECTION_REQUEST, Bundle().apply {
                            putParcelable(SELECTED_OBJECT_KEY, clickedObject)
                        })
                        findNavController().popBackStack()
                    },
                    onObjectEditClickedCallback = null,
                    onObjectDeleteClickedCallback = null,
                    onAddRelationClickedCallback = null
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllObjects()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentObjectSelectionBinding = FragmentObjectSelectionBinding
        .inflate(inflater, container, false)
}