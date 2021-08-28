package com.aliakseipalianski.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list_view.*

class ListActivity : AppCompatActivity(R.layout.activity_list_view) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // any realisation of ListAdapter
        listView.adapter = object : ArrayAdapter<Item>(
            this,
            0,
            Item.generate()
        ) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ) = convertView ?: createConvertView(parent).apply {
                getItem(position)?.let {
                    bind(this, it)
                }
            }

            fun bind(
                view: View,
                item: Item
            ) {
                view.findViewById<TextView>(R.id.firstName).text = item.firstName
                view.findViewById<TextView>(R.id.lastName).text = item.lastName
            }

            fun createConvertView(
                parent: ViewGroup
            ) = LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false)
        }
    }
}