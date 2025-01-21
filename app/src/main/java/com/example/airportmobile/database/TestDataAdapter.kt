package com.example.airportmobile.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.airportmobile.R


class TestDataAdapter(private val data: List<TestDataEntity>) :
    RecyclerView.Adapter<TestDataAdapter.TestDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return TestDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestDataViewHolder, position: Int) {
        val item = data[position]
        holder.location.text = "Location: ${item.locationName}"
        holder.crowdNumber.text = "Crowd: ${item.crowdNumber}"
        holder.timestamp.text = "Timestamp: ${item.timestamp}"
    }

    override fun getItemCount(): Int = data.size

    class TestDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.textViewLocation)
        val crowdNumber: TextView = itemView.findViewById(R.id.textViewCrowdNumber)
        val timestamp: TextView = itemView.findViewById(R.id.textViewTimestamp)
    }
}


