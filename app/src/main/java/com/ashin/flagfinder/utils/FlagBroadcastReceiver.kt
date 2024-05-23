package com.ashin.flagfinder.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.ashin.flagfinder.MainActivity

class FlagBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0, "Broadcast received", Toast.LENGTH_SHORT).show()
        (p0 as MainActivity).let {
            it.updateUiForStart()
        }
    }
}