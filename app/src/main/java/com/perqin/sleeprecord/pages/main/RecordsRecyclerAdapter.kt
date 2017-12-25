package com.perqin.sleeprecord.pages.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.perqin.sleeprecord.R
import com.perqin.sleeprecord.app.App
import com.perqin.sleeprecord.data.models.record.Record
import com.perqin.sleeprecord.utils.dateandtime.timestampToLocalTime
import kotlinx.android.synthetic.main.item_main_record.view.*

/**
 * Created on 12/25/17.
 *
 * @author perqin
 */
class RecordsRecyclerAdapter : RecyclerView.Adapter<RecordsRecyclerAdapter.ViewHolder>() {
    private var records = emptyList<Record>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main_record, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.durationTextView.text = App.context.getString(R.string.textView_main_duration,
                timestampToLocalTime(record.start), timestampToLocalTime(record.end))
    }

    override fun getItemCount() = records.size

    fun updateRecords(records: List<Record>) {
        this.records = records
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val durationTextView = itemView.textView_record!!
    }
}
