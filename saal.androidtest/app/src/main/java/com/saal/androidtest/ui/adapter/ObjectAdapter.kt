package com.saal.androidtest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saal.androidtest.R
import com.saal.androidtest.databinding.ItemAddRelationBinding
import com.saal.androidtest.databinding.ItemNoEntriesBinding
import com.saal.androidtest.databinding.ItemObjectBinding
import com.saal.androidtest.domain.model.ObjectModel

class ObjectAdapter(
    private var objectList: MutableList<ObjectListModel>,
    private val onObjectClickedCallback: ((objectModel: ObjectModel) -> Unit)? = null,
    private val onObjectEditClickedCallback: ((objectModel: ObjectModel) -> Unit)? = null,
    private val onObjectDeleteClickedCallback: ((objectModel: ObjectModel) -> Unit)? = null,
    private val onAddRelationClickedCallback: (() -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    data class ObjectListModel(
        val viewType: ViewType = ViewType.ObjectItem(),
        val data: ObjectModel? = null,
    )

    sealed class ViewType(val ordinal: Int, open val entriesStringRes: Int = -1) {
        data class ObjectItem(override val entriesStringRes: Int = -1) :
            ViewType(ordinal = 0, entriesStringRes = entriesStringRes)

        data class ObjectSelectionItem(override val entriesStringRes: Int = -1) :
            ViewType(ordinal = 1, entriesStringRes = entriesStringRes)

        data class AddRelationItem(override val entriesStringRes: Int = -1) :
            ViewType(ordinal = 2, entriesStringRes = entriesStringRes)

        data class NoEntriesItem(override val entriesStringRes: Int = -1) :
            ViewType(ordinal = 3, entriesStringRes = entriesStringRes)
    }

    inner class ObjectViewHolder(private val binding: ItemObjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(objectModel: ObjectListModel) {
            val title = "${objectModel.data?.type}: ${objectModel.data?.name}"

            binding.titleTv.text = title
            binding.detailsTv.text = objectModel.data?.description

            onObjectClickedCallback?.let { callback ->
                binding.root.setOnClickListener {
                    objectModel.data?.let { objectModel ->
                        callback(objectModel)
                    }
                }
            }
            onObjectEditClickedCallback?.let { callback ->
                binding.editBt.setOnClickListener {
                    objectModel.data?.let { objectModel ->
                        callback(objectModel)
                    }
                }
            }
            onObjectDeleteClickedCallback?.let { callback ->
                binding.deleteBt.setOnClickListener {
                    objectModel.data?.let { objectModel ->
                        callback(objectModel)
                    }
                }
            }
        }

        fun clearViewHolderState() {
            binding.titleTv.text = ""
            binding.detailsTv.text = ""
            showEditOptions(true)
        }

        fun showEditOptions(show: Boolean) {
            if (show) {
                binding.editBt.visibility = View.VISIBLE
                binding.deleteBt.visibility = View.VISIBLE
            } else {
                binding.editBt.visibility = View.GONE
                binding.deleteBt.visibility = View.GONE
            }
        }
    }

    inner class AddRelationViewHolder(binding: ItemAddRelationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            onAddRelationClickedCallback?.let { callback ->
                binding.root.setOnClickListener {
                    callback()
                }
                binding.addBt.setOnClickListener {
                    callback()
                }
            }
        }
    }

    inner class NoEntriesViewHolder(private val binding: ItemNoEntriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(objectListModel: ObjectListModel) {
            with(binding.root.context) {
                binding.titleTv.text = getString(
                    R.string.no_list_entries_to_display,
                    getString(objectListModel.viewType.entriesStringRes).lowercase()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.ObjectItem().ordinal -> {
                val binding = ItemObjectBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ObjectViewHolder(binding)
            }

            ViewType.ObjectSelectionItem().ordinal -> {
                val binding = ItemObjectBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ObjectViewHolder(binding)
            }

            ViewType.AddRelationItem().ordinal -> {
                val binding = ItemAddRelationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddRelationViewHolder(binding)
            }

            ViewType.NoEntriesItem().ordinal -> {
                val binding = ItemNoEntriesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                NoEntriesViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return objectList[position].viewType.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.ObjectItem().ordinal -> {
                with(holder as ObjectViewHolder) {
                    bind(objectList[position])
                    showEditOptions(true)
                }
            }

            ViewType.ObjectSelectionItem().ordinal -> {
                with(holder as ObjectViewHolder) {
                    bind(objectList[position])
                    showEditOptions(false)
                }
            }

            ViewType.AddRelationItem().ordinal -> {

            }

            ViewType.NoEntriesItem().ordinal -> {
                (holder as NoEntriesViewHolder).bind(objectList[position])
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ObjectViewHolder) {
            holder.clearViewHolderState()
        }
    }


    override fun getItemCount(): Int {
        return objectList.size
    }

    fun getObjects(): List<ObjectModel> {
        val objectsListModels = objectList.map { it.data }
        return objectsListModels.filterNotNull()
    }

    fun clearData() {
        val maxSize = objectList.size
        objectList = mutableListOf()
        notifyItemRangeRemoved(0, maxSize)
    }

    fun setData(conversations: List<ObjectListModel>) {
        val prevDataSize = objectList.size
        objectList = conversations.toMutableList()
        notifyItemRangeRemoved(0, prevDataSize)
        notifyItemRangeInserted(0, conversations.size)
    }

    fun insertData(conversations: List<ObjectListModel>) {
        if (conversations.isEmpty()) return
        val firstIndex = objectList.size
        objectList.addAll(conversations)
        notifyItemRangeInserted(firstIndex, conversations.size)
    }
}