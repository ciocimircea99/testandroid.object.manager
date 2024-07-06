package com.saal.androidtest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saal.androidtest.R
import com.saal.androidtest.databinding.FragmentAddObjectBinding
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.ui.adapter.ObjectAdapter
import com.saal.androidtest.ui.base.BaseFragment
import com.saal.androidtest.ui.fragment.ObjectSelectionFragment.Companion.OBJECT_SELECTION_REQUEST
import com.saal.androidtest.ui.fragment.ObjectSelectionFragment.Companion.SELECTED_OBJECT_KEY
import com.saal.androidtest.ui.viewmodel.AddObjectViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddObjectFragment : BaseFragment<FragmentAddObjectBinding>() {

    override val viewModel by sharedViewModel<AddObjectViewModel>()

    private lateinit var objectAdapter: ObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.relatedObjects.observe(viewLifecycleOwner) { relatedObjects ->
            binding.relationsRv.apply {
                layoutManager = LinearLayoutManager(context)

                val uiList = relatedObjects.map {
                    ObjectAdapter.ObjectListModel(
                        viewType = ObjectAdapter.ViewType.ObjectItem(),
                        data = it
                    )
                }.toMutableList()
                if (uiList.isEmpty()) {
                    uiList.add(
                        ObjectAdapter.ObjectListModel(
                            viewType = ObjectAdapter.ViewType.NoEntriesItem(R.string.relations),
                            data = null
                        )
                    )
                }
                uiList.add(
                    ObjectAdapter.ObjectListModel(
                        viewType = ObjectAdapter.ViewType.AddRelationItem(),
                        data = null
                    )
                )
                objectAdapter = ObjectAdapter(
                    uiList,
                    onObjectClickedCallback = null,
                    onObjectEditClickedCallback = null,
                    onObjectDeleteClickedCallback = { viewModel.removeRelatedObject(it) },
                    onAddRelationClickedCallback = { navForObjectSelection() }
                )
                adapter = objectAdapter
            }
        }
        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is AddObjectViewModel.Command.Finish -> {
                    findNavController().popBackStack()
                }
            }
        }
        binding.addButton.setOnClickListener {
            viewModel.addObject(
                ObjectModel(
                    name = binding.nameEt.text.toString(),
                    description = binding.descriptionEt.text.toString(),
                    type = binding.typeEt.text.toString()
                )
            )
        }
    }

    private fun navForObjectSelection() {
        setFragmentResultListener(OBJECT_SELECTION_REQUEST) { key, bundle ->
            val selectedObject = bundle.getParcelable<ObjectModel>(SELECTED_OBJECT_KEY)
            selectedObject?.let { viewModel.addRelatedObject(it) }
        }
        findNavController().navigate(
            AddObjectFragmentDirections.actionAddObjectFragmentToObjectSelectionFragment()
        )
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddObjectBinding = FragmentAddObjectBinding
        .inflate(inflater, container, false)
}