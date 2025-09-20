package vn.tutorial.simplealarmandroid.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vn.tutorial.simplealarmandroid.R
import vn.tutorial.simplealarmandroid.common.constants.Tag
import vn.tutorial.simplealarmandroid.common.helpers.AlarmHelper
import vn.tutorial.simplealarmandroid.common.helpers.IconHelper
import vn.tutorial.simplealarmandroid.common.helpers.TimeConverter
import vn.tutorial.simplealarmandroid.domain.model.AlarmModel

class AlarmAdapter(
    private val deleteAlarm: (AlarmModel) -> Unit,
    private val editAlarm: (AlarmModel) -> Unit,
    private val enableAlarm: (AlarmModel) -> Unit

) : ListAdapter<AlarmModel, AlarmAdapter.AlarmViewHolder>(DiffCallback) {

    inner class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card_item)
        val nextAlarm: TextView = view.findViewById(R.id.next_alarm)
        val imgDayNight: ImageView = view.findViewById(R.id.day_or_night)
        val tvTime: TextView = view.findViewById(R.id.tv_time)
        val tvDates: TextView = view.findViewById(R.id.tv_dates)
        val switchOnOff: SwitchCompat = view.findViewById(R.id.on_off)
        val btnEdit: ImageButton = view.findViewById(R.id.btn_edit)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_delete)

        fun bind(item: AlarmModel) {
            Log.d(Tag.AlarmTag, "bind item")
            // set dữ liệu
            tvTime.text = TimeConverter.convertTimeToString(item.hour, item.minute)
            tvDates.text = TimeConverter.convertListDateToString(item.dateOfWeek)

            nextAlarm.text = nextDateOfAlarm(item)

            imgDayNight.setImageResource(
                IconHelper.getIconResourceForAlarm(item.hour)
            )

            switchOnOff.setOnCheckedChangeListener(null)
            switchOnOff.isChecked = item.isOn
            listOf(card, imgDayNight, tvTime, tvDates, btnEdit, btnDelete).forEach {
                it.isSelected = item.isOn
            }
            switchOnOff.setOnCheckedChangeListener { _, isChecked ->
                Log.d(Tag.AlarmTag, "Switch checked: $isChecked")
                item.isOn = isChecked
                listOf(card, imgDayNight, tvTime, tvDates, btnEdit, btnDelete).forEach {
                    it.isSelected = item.isOn
                }
                enableAlarm(item)
            }

            // gán sự kiện
            btnDelete.setOnClickListener { deleteAlarm(item) }
            btnEdit.setOnClickListener { editAlarm(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AlarmModel>() {
        override fun areItemsTheSame(oldItem: AlarmModel, newItem: AlarmModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlarmModel, newItem: AlarmModel): Boolean {
            return oldItem == newItem
        }
    }

    fun nextDateOfAlarm(item: AlarmModel): String {
        return if (!item.dateOfWeek.isNullOrEmpty()) {
            TimeConverter.nameDateOfWeek(
                AlarmHelper.getNextDayOfWeek(
                    item.hour,
                    item.minute,
                    item.dateOfWeek!!
                )
            )
        } else if (item.date != null) {
            TimeConverter.dayMonthYearFormatFromCalendar(item.date!!)
        } else {
            ""
        }
    }
}
