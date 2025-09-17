package vn.tutorial.simplealarmandroid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import vn.tutorial.simplealarmandroid.common.constants.Tag

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d(Tag.AlarmTag, "Receive alarm")
    }
}