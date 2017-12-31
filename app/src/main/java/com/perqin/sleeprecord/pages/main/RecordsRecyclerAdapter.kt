package com.perqin.sleeprecord.pages.main

import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.perqin.sleeprecord.R
import com.perqin.sleeprecord.app.App
import kotlinx.android.synthetic.main.item_main_record.view.*

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
class RecordsRecyclerAdapter : RecyclerView.Adapter<RecordsRecyclerAdapter.ViewHolder>() {
    var records = emptyList<Record>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var durationMin = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var durationMax = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main_record, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        val color = ContextCompat.getColor(App.context, when(record.health) {
            Record.Health.GOOD -> R.color.green500
            Record.Health.MEDIUM -> R.color.orange500
            Record.Health.BAD -> R.color.red500
        })
        holder.dayTextView.text = record.day.toString()
        holder.dayTextView.backgroundTintList = ColorStateList.valueOf(color)
        holder.durationTextView.text = App.context.getString(
                R.string.textView_main_duration,
                App.context.getString(if (record.startH >= 24) R.string.textView_main_durationTimeNextDay else R.string.textView_main_durationTimeToday, record.startH % 24, record.startM),
                App.context.getString(if (record.endH >= 24) R.string.textView_main_durationTimeNextDay else R.string.textView_main_durationTimeToday, record.endH % 24, record.endM)
        )
        holder.durationView.min = durationMin
        holder.durationView.max = durationMax
        holder.durationView.durationStart = record.start
        holder.durationView.durationEnd = record.end
        holder.durationView.color = color
    }

    override fun getItemCount() = records.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView = itemView.textView_day!!
        val durationTextView = itemView.textView_record!!
        val durationView = itemView.durationView!!
    }
}
