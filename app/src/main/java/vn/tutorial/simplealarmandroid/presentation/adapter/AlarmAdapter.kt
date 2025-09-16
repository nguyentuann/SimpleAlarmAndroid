package vn.tutorial.simplealarmandroid.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.common.helpers.TimeConverter
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel

class AlarmAdapter(private val items: List<AlarmModel>) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = itemView.findViewById(R.id.card_item)
        val nextAlarm: TextView = itemView.findViewById(R.id.next_alarm)
        val imgDayNight: ImageView = view.findViewById(R.id.day_or_night)
        val tvTime: TextView = view.findViewById(R.id.tv_time)
        val tvDates: TextView = view.findViewById(R.id.tv_dates)
        val switchOnOff: SwitchCompat = view.findViewById(R.id.on_off)
        val btnEdit: ImageButton = view.findViewById(R.id.btn_edit)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val item = items[position]
        holder.tvTime.text = TimeConverter.convertTimeToString(item.hour, item.minute)
        holder.tvDates.text = TimeConverter.convertListDateToString(item.dateOfWeek)



        if (item.hour in 6..18) {
            holder.imgDayNight.setImageResource(R.drawable.ic_day)
        } else {
            holder.imgDayNight.setImageResource(R.drawable.ic_night)
        }

        holder.switchOnOff.isChecked = item.isOn

        holder.card.isSelected = item.isOn
        holder.imgDayNight.isSelected = item.isOn
        holder.tvTime.isSelected = item.isOn
        holder.tvDates.isSelected = item.isOn
        holder.btnEdit.isSelected = item.isOn
        holder.btnDelete.isSelected = item.isOn

    }

    override fun getItemCount(): Int {
        return items.size
    }
}