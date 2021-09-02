package com.aliakseipalianski.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_recycler.*
import java.util.concurrent.Executors

class RecyclerActivity : AppCompatActivity(R.layout.activity_recycler) {

    private val list: MutableList<Item> = Item.generate().toMutableList()

    // TODO click listener --- DONE
    // TODO how to update list -- DONE
    // TODO diff utils -- +/- Дописать в проект https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    // TODO swap to action for example https://www.journaldev.com/23164/android-recyclerview-swipe-to-delete-undo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        //recyclerView.layoutManager = LinearLayoutManager(this) can be defined in xml
        recyclerView.adapter = ItemAdapter(
            list,
            ::removeElementListener
        )

        Executors.newCachedThreadPool()

        //example with DiffUtils
        //   val adapterWithDiffer = ItemDiffAdapter()
        //   recyclerView.adapter = adapterWithDiffer
        //   adapterWithDiffer.submitList(list)
    }

    private fun removeElementListener(id: Int) {
        val itemPosition = list.indexOfLast { item ->
            item.id == id
        }


        val newList = list.apply {
            removeAt(itemPosition)
        }

        (recyclerView.adapter as? ItemAdapter)?.apply {
            itemList = newList
            notifyItemRemoved(itemPosition)
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}

class ItemDiffAdapter : ListAdapter<Item, ItemDiffAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.view_item, parent, false)
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        private val firstNameView: TextView = view.findViewById(R.id.firstName)
        private val lastNameView: TextView = view.findViewById(R.id.lastName)

        fun bind(item: Item) {
            firstNameView.text = item.firstName
            lastNameView.text = item.lastName
        }
    }
}

class ItemAdapter(var itemList: List<Item>, private val deleteListener: (Int) -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = ItemViewHolder(
        LayoutInflater.from(
            parent.context
        ).inflate(R.layout.view_item, parent, false),
        deleteListener
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.unbind()
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    class ItemViewHolder(
        view: View,
        private val deleteListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        class onDeleteListener(
            private val deleteListener: (Int) -> Unit
        ) : View.OnClickListener {

            var item: Item? = null

            override fun onClick(v: View?) {
                item?.let {
                    deleteListener.invoke(it.id)
                }
            }
        }

        private val onDelete = onDeleteListener(deleteListener)
        private val firstNameView: TextView = view.findViewById(R.id.firstName)
        private val lastNameView: TextView = view.findViewById(R.id.lastName)

        fun bind(item: Item) {
            firstNameView.text = item.firstName

            if (item.id > 10 || item.id > 30) {
                if (item.id % 2 != 0) {
                    lastNameView.text = item.lastName
                }
            } else if (item.id > 20 || item.id > 40) {
                if (item.id % 2 == 0) {
                    lastNameView.text = item.lastName
                }
            }

            onDelete.item = item

            itemView.setOnClickListener(onDelete)
        }

        fun unbind() {
            onDelete.item = null
            lastNameView.text = null
            lastNameView.text = null
        }
    }
}