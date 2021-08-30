package com.aliakseipalianski.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list_view.*

const val VIEW_TYPE_IMAGE = "image"
const val VIEW_TYPE_VIDEO = "video"

class ViewHolder(view: View) {

    private val textView1: TextView
    private val textView2: TextView

    init {
        textView1 = view.findViewById(R.id.firstName)
        textView2 = view.findViewById(R.id.lastName)
    }

    fun bind(
        item: Item
    ) {
        textView1.text = item.firstName
        textView2.text = item.lastName
    }

}

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
                    (this.tag as? ViewHolder)?.bind(it)
                }
            }

            fun createConvertView(
                parent: ViewGroup,
                item: Item? = null
            ): View {
                if (item?.type == VIEW_TYPE_IMAGE) {
                    // LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false)
                    // different view type
                    //return View()
                    throw  Exception()
                } else {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_item, parent, false)
                    val viewHolder = ViewHolder(view)
                    view.tag = viewHolder

                    return view
                }
            }
        }
    }
}