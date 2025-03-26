package com.electrosim.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.electrosim.R

class ImageAdapter(
    private val context: Context,
    private val mobileValues: Array<String>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        
        val gridView = convertView ?: inflater.inflate(R.layout.mobile, null).also { view ->
            // Set value into textview
            val textView = view.findViewById<TextView>(R.id.grid_item_label)
            textView.text = mobileValues[position]

            // Set image based on selected text
            val imageView = view.findViewById<ImageView>(R.id.grid_item_image)
            
            when (mobileValues[position]) {
                "Windows" -> imageView.setImageResource(R.drawable.not)
                "iOS" -> imageView.setImageResource(R.drawable.not)
                "Blackberry" -> imageView.setImageResource(R.drawable.not)
                else -> imageView.setImageResource(R.drawable.not)
            }
        }
        
        return gridView
    }

    override fun getCount(): Int {
        return mobileValues.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
} 