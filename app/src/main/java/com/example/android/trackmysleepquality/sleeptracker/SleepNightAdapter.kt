package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding


/**
 * Here the adapter extends the RecyclerView.Adapter class. We have another adpater which extends
 * ListAdapter instead.
 */
class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.SleepNightViewHolder>() {
    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //the constructor is private. Because from() is now a method that returns a new ViewHolder instance,
    // there's no reason for anyone to call the constructor of ViewHolder anymore
    class SleepNightViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sleepNight: SleepNight, res: Resources) {
            binding.qualityString.text = convertNumericQualityToString(sleepNight.sleepQuality, res)
            binding.sleepLength.text = convertDurationToFormatted(sleepNight.startTimeMilli, sleepNight.endTimeMilli, res)
            binding.qualityImage.setImageResource(when (sleepNight.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
        }

        companion object {
            fun from(parent: ViewGroup) : SleepNightViewHolder {
                val layoutinflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutinflater, parent, false)
                //val rootView = layoutinflater.inflate(R.layout.list_item_sleep_night, parent, false)
                return SleepNightViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightViewHolder {
        return SleepNightViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SleepNightViewHolder, position: Int) {
        val item = data.get(position)
        val res = holder.itemView.context.resources
        holder.bind(item, res)
    }
}