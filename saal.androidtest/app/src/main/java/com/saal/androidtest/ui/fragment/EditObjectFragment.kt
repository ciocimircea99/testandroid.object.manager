package com.saal.androidtest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.saal.androidtest.R
import com.saal.androidtest.databinding.FragmentEditObjectBinding
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.ui.adapter.ObjectAdapter
import com.saal.androidtest.ui.base.BaseFragment
import com.saal.androidtest.ui.fragment.ObjectSelectionFragment.Companion.OBJECT_SELECTION_REQUEST
import com.saal.androidtest.ui.fragment.ObjectSelectionFragment.Companion.SELECTED_OBJECT_KEY
import com.saal.androidtest.ui.viewmodel.EditObjectViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditObjectFragment : BaseFragment<FragmentEditObjectBinding>() {

    override val viewModel by sharedViewModel<EditObjectViewModel>()

    private val args: EditObjectFragmentArgs by navArgs()

    private lateinit var objectAdapter: ObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initObjectToEdit(args.objectModel)

        viewModel.objectModel.observe(viewLifecycleOwner) { objectModel ->
            binding.nameEt.setText(objectModel.name)
            binding.descriptionEt.setText(objectModel.description)
            binding.typeEt.setText(objectModel.type)
        }
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
                    onObjectEditClickedCallback = { clickedObject ->
                        viewModel.getObjectWithRelations(clickedObject)
                    },
                    onObjectDeleteClickedCallback = { clickedObject ->
                        viewModel.deleteRelation(clickedObject)
                    },
                    onAddRelationClickedCallback = {
                        navForAddRelation()
                    }
                )
                adapter = objectAdapter
            }
        }
        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is EditObjectViewModel.Command.Finish -> {
                    findNavController().popBackStack()
                }
            }
        }
        binding.addButton.setOnClickListener {
            viewModel.updateObject(
                name = binding.nameEt.text.toString(),
                description = binding.descriptionEt.text.toString(),
                type = binding.typeEt.text.toString()
            )
        }
    }

    private fun navForAddRelation() {
        viewModel.saveEditedObjectState(
            name = binding.nameEt.text.toString(),
            description = binding.descriptionEt.text.toString(),
            type = binding.typeEt.text.toString()
        )
        setFragmentResultListener(OBJECT_SELECTION_REQUEST) { key, bundle ->
            val selectedObject = bundle.getParcelable<ObjectModel>(SELECTED_OBJECT_KEY)
            selectedObject?.let { viewModel.addRelation(it) }
        }
        findNavController().navigate(
            EditObjectFragmentDirections.actionEditObjectFragmentToObjectSelectionFragment()
        )
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditObjectBinding = FragmentEditObjectBinding
        .inflate(inflater, container, false)
}