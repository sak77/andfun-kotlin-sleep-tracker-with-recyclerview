/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

/**
 * The adapter acts as an interface between the recycler view and the data to be displayed in the recycler view.
 * It works with the viewholder class which is the actual implementation of the logic to map the data to the views.
 * It provides 3 methods -
 * getItemCount - returns number of records to display in the viewholder
 * onCreateViewHolder - returns instance of the viewholder class
 * onBindViewHolder - binds viewholder data with the respetive list item layout.
 *
 * ListAdapter derives from Recyclerview.Adapter.
 */
class SleepNightListAdapter (val clickListener: SleepNightClickListener) : ListAdapter<SleepNight, SleepNightListAdapter.MySleepNightViewHolder>(SleepNightDiffCallback()) {

    /*
    Inside the adapter class you can delete instance that holds the list data including any setter because ListAdapter keeps track of the list for you.
    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySleepNightViewHolder {
        return MySleepNightViewHolder.from(parent)
    }
    /*
    Delete the override of getItemCount(), because the ListAdapter implements this method for you.
    override fun getItemCount(): Int {
        return itemCount
    }*/

    override fun onBindViewHolder(holder: MySleepNightViewHolder, position: Int) {
        val item = getItem(position)
        val res = holder.itemView.context.resources
        holder.bind(item, res, clickListener)
    }

    //Viewholder constructor is private since we provide a from method in the companion object.
    //So another class does not need the constructor to instantiate this class...
    class MySleepNightViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        //Move binding logic to viewholder
        fun bind(item: SleepNight, res: Resources, clickListener: SleepNightClickListener) {

            /* Using BindingAdapter we can omit the following binding code
            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)
            binding.qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })*/

            binding.sleep = item
            binding.clicklistener = clickListener
            binding.executePendingBindings()
        }
        //Layout inflater logic can also be part of the viewholder
        companion object {
            fun from(parent: ViewGroup): MySleepNightViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                //val rootView = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                val sleepNightBinding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                //return MySleepNightViewHolder(rootView)
                return MySleepNightViewHolder(sleepNightBinding)
            }
        }
    }
}

//DiffUtils has a class called ItemCallback that you extend to figure our difference between two items in a list
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}

//Define a clicklistener that takes a lambda as an argument and passes the night Id on click
class SleepNightClickListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}
