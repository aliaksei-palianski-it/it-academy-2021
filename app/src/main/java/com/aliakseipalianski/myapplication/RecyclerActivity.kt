package com.aliakseipalianski.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity(R.layout.activity_recycler) {

    // TODO click listener
    // TODO how to update list
    // TODO diff utils
    // TODO swap to action for example https://www.journaldev.com/23164/android-recyclerview-swipe-to-delete-undo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        //recyclerView.layoutManager = LinearLayoutManager(this) can be defined in xml
        recyclerView.adapter = ItemAdapter(Item.generate())
    }
}

class ItemAdapter(var itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val firstNameView: TextView = view.findViewById(R.id.firstName)
        private val lastNameView: TextView = view.findViewById(R.id.lastName)

        fun bind(item: Item) {
            firstNameView.text = item.firstName
            lastNameView.text = item.lastName
        }
    }
}