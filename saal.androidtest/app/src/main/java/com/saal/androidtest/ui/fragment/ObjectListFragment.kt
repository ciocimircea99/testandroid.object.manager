package com.saal.androidtest.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saal.androidtest.R
import com.saal.androidtest.databinding.FragmentObjectListBinding
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.ui.adapter.ObjectAdapter
import com.saal.androidtest.ui.base.BaseFragment
import com.saal.androidtest.ui.viewmodel.ObjectListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ObjectListFragment : BaseFragment<FragmentObjectListBinding>() {

    override val viewModel by viewModel<ObjectListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            navToAddObject()
        }
        binding.searchEt.doAfterTextChanged { text ->
            viewModel.filterEntries(text.toString())
        }

        viewModel.objectList.observe(viewLifecycleOwner) { objects ->
            binding.objectsRv.apply {
                layoutManager = LinearLayoutManager(context)

                val uiList = objects.map {
                    ObjectAdapter.ObjectListModel(
                        viewType = ObjectAdapter.ViewType.ObjectItem(),
                        data = it
                    )
                }.toMutableList()
                if (uiList.isEmpty()) {
                    uiList.add(
                        ObjectAdapter.ObjectListModel(
                            viewType = ObjectAdapter.ViewType.NoEntriesItem(R.string.objects),
                            data = null
                        )
                    )
                }
                adapter = ObjectAdapter(
                    uiList,
                    onObjectClickedCallback = null,
                    onObjectEditClickedCallback = { navForEditObject(it) },
                    onObjectDeleteClickedCallback = { clickedObject ->
                        showDeleteConfirmationDialog(context) {
                            viewModel.deleteObject(clickedObject)
                        }
                    },
                    onAddRelationClickedCallback = null
                )
            }
        }
    }

    private fun navForEditObject(clickedObject: ObjectModel) {
        val action = ObjectListFragmentDirections.actionObjectListFragmentToEditObjectFragment(
            clickedObject
        )
        findNavController().navigate(action)
    }

    private fun navToAddObject() {
        val action = ObjectListFragmentDirections.actionObjectListFragmentToAddObjectFragment()
        findNavController().navigate(action)
    }

    private fun showDeleteConfirmationDialog(context: Context, onDeleteConfirmed: () -> Unit) {
        AlertDialog.Builder(context).apply {
            setTitle(getString(R.string.delete_confirmation))
            setMessage(getString(R.string.are_you_sure_delete))
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(getString(R.string.yes_delete)) { dialog, _ ->
                onDeleteConfirmed()
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllObjects()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentObjectListBinding = FragmentObjectListBinding
        .inflate(inflater, container, false)
}