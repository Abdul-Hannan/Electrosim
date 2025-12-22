package com.electrosim.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.electrosim.R
import com.electrosim.storage.CircuitData
import com.electrosim.storage.CircuitStorageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SavedCircuitsAdapter(
    private val context: Context,
    private var circuits: List<CircuitData>,
    private val storageManager: CircuitStorageManager,
    private val onDeleteClick: ((CircuitData) -> Unit)? = null,
    private val onDuplicateClick: ((CircuitData) -> Unit)? = null
) : BaseAdapter() {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    private val thumbnailCache = mutableMapOf<String, Bitmap?>()

    override fun getCount(): Int = circuits.size

    override fun getItem(position: Int): CircuitData = circuits[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_saved_circuit, parent, false)

        val circuit = circuits[position]

        // Set circuit name
        val nameTextView = view.findViewById<TextView>(R.id.circuit_name)
        nameTextView.text = circuit.name

        // Set date
        val dateTextView = view.findViewById<TextView>(R.id.circuit_date)
        dateTextView.text = dateFormat.format(Date(circuit.updatedAt))

        // Set component count info
        val infoTextView = view.findViewById<TextView>(R.id.circuit_info)
        val elementCount = circuit.elements.size
        val connectionCount = circuit.connections.size
        infoTextView.text = "$elementCount components, $connectionCount wires"

        // Set thumbnail
        val thumbnailImageView = view.findViewById<ImageView>(R.id.circuit_thumbnail)
        val thumbnail = getThumbnail(circuit.id)
        if (thumbnail != null) {
            thumbnailImageView.setImageBitmap(thumbnail)
        } else {
            thumbnailImageView.setImageResource(R.drawable.ic_circuit_placeholder)
        }

        // Set up duplicate button
        val duplicateButton = view.findViewById<ImageView>(R.id.duplicate_button)
        duplicateButton.setOnClickListener {
            onDuplicateClick?.invoke(circuit)
        }

        // Set up delete button
        val deleteButton = view.findViewById<ImageView>(R.id.delete_button)
        deleteButton.setOnClickListener {
            onDeleteClick?.invoke(circuit)
        }

        return view
    }

    private fun getThumbnail(circuitId: String): Bitmap? {
        if (!thumbnailCache.containsKey(circuitId)) {
            thumbnailCache[circuitId] = storageManager.getThumbnail(circuitId)
        }
        return thumbnailCache[circuitId]
    }

    fun updateCircuits(newCircuits: List<CircuitData>) {
        circuits = newCircuits
        thumbnailCache.clear()
        notifyDataSetChanged()
    }
}
