package com.ashin.flagfinder.view.fragment

import android.icu.util.Calendar
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ashin.flagfinder.R
import com.ashin.flagfinder.databinding.SheduleChallengeFragmentBinding
import com.ashin.flagfinder.utils.PreferenceManger
import com.ashin.flagfinder.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

//@AndroidEntryPoint
//class SheduleChalengeFragment : Fragment(){
//    private lateinit var binding: SheduleChallengeFragmentBinding
//    private lateinit var calendar: Calendar
//    private val viewModel:MainViewModel by viewModels()
//    private var sheduledstartTime:Long?=null
//    private var startTime:Long?=null
//    lateinit var timer:CountDownTimer
//    @Inject
//    lateinit var preferenceManger: PreferenceManger
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding= SheduleChallengeFragmentBinding.inflate(inflater,container,false)
//        initUi()
//        return binding.root
//
//    }
//
//    private fun initUi() {
//        if(preferenceManger.getStartTime()!=0L)
//        {
//            updateUi(preferenceManger.getStartTime()!!)
//        }
//        binding.llMain.btnSave.setOnClickListener {
//            if(validate())
//            {
//                scheduleTime()
//            }
//        }
////        viewModel._updateUI.observe(viewLifecycleOwner, Observer{
////            if (it==true){
////               updateUi()
////             }
////        })
//    }
//
//    private fun updateUi(sheduledstartTime:Long) {
//        binding.llMain.llshedule.isVisible=false
//        binding.llMain.llstart.isVisible=true
//        timer=object : CountDownTimer(sheduledstartTime!!-System.currentTimeMillis(),1000){
//            override fun onTick(p0: Long) {
//                val numberFormat= DecimalFormat("00")
//                val hour= (p0 / 3600000) % 24
//                val min= (p0 / 60000) % 60
//                val sec= (p0 / 1000) % 60
//                binding.llMain.tvTimer.text = (numberFormat.format(hour) + ":" + numberFormat.format(min)).toString() + ":" + numberFormat.format(sec)
//            }
//
//            override fun onFinish() {
//                preferenceManger.setLastTime(0)
//                findNavController().navigate(R.id.action_sheduleChalengeFragment_to_flagsChallemgeFragment)
//            }
//
//        }
//        timer.start()
//
//    }
//
//    private fun validate():Boolean
//    {
//        var isValid=false
//        if(binding.llMain.etH1.text.toString().trim().isEmpty())
//        {
//            Toast.makeText(activity,"Please enter a valid hour",Toast.LENGTH_SHORT).show()
//        }
//        else if(binding.llMain.etH2.text.toString().trim().isEmpty())
//        {
//            Toast.makeText(activity,"Please enter a valid hour",Toast.LENGTH_SHORT).show()
//        }
//        else if(binding.llMain.etM1.text.toString().trim().isEmpty())
//        {
//            Toast.makeText(activity,"Please enter a valid minute",Toast.LENGTH_SHORT).show()
//        }
//        else if(binding.llMain.etM2.text.toString().trim().isEmpty())
//        {
//            Toast.makeText(activity,"Please enter a valid minute",Toast.LENGTH_SHORT).show()
//        }
//        else if(binding.llMain.etS1.text.toString().trim().isEmpty())
//        {
//            Toast.makeText(activity,"Please enter a valid second",Toast.LENGTH_SHORT).show()
//        }
//        else if(binding.llMain.etS2.text.toString().trim().isEmpty())
//        {
//            Toast.makeText(activity,"Please enter a valid second",Toast.LENGTH_SHORT).show()
//        }
//        else
//        {
//            isValid=true
//        }
//        return isValid
//    }
//    private fun scheduleTime() {
//        val h1= binding.llMain.etH1.text.toString().trim()
//        val h2= binding.llMain.etH2.text.toString().trim()
//        val m1= binding.llMain.etM1.text.toString().trim()
//        val m2= binding.llMain.etM2.text.toString().trim()
//        val s1= binding.llMain.etS1.text.toString().trim()
//        val s2= binding.llMain.etS2.text.toString().trim()
//        val hour= (h1 + h2).toInt()
//        val minute= (m1 + m2).toInt()
//        val second= (s1 + s2).toInt()
////        val alarmManager= activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        val intent= Intent(activity, FlagBroadcastReceiver::class.java)
////        val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
////        val calendar= Calendar.getInstance().apply {
////            set(Calendar.HOUR_OF_DAY, hour)
////            set(Calendar.MINUTE, minute)
////            set(Calendar.SECOND, second)
////        }
////        if (calendar.timeInMillis <= System.currentTimeMillis()) {
////            calendar.add(Calendar.DAY_OF_YEAR, 1)
////        }
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
////            if (alarmManager.canScheduleExactAlarms()) {
////                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
////            } else {
////                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
////                startActivity(intent)
////            }
////        } else {
////            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
////        }
//        val startSwithin="$hour:$minute:$second"
//        sheduledstartTime= parseTimeToMillis(startSwithin)
//        preferenceManger.setStartTime(sheduledstartTime)
//        updateUi(sheduledstartTime!!)
//
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            requireActivity().finish()
//        }
//    }
//    private fun parseTimeToMillis(timeStr: String): Long {
//        try {
//            val sdf: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//            val date: Date = sdf.parse(timeStr)
//            val targetTimeMillis: Long = date.getTime()
//            val currentTimeMillis = System.currentTimeMillis()
//            val elapsedTime: Long = targetTimeMillis - sdf.parse("00:00:00").getTime()
//            return currentTimeMillis + elapsedTime
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return 0
//    }
//
//    override fun onPause() {
//        super.onPause()
////        timer.cancel()
//    }
//
//
//}
@AndroidEntryPoint
class SheduleChalengeFragment : Fragment() {
    private lateinit var binding: SheduleChallengeFragmentBinding
    private var timer: CountDownTimer? = null
    @Inject
    lateinit var preferenceManger: PreferenceManger

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SheduleChallengeFragmentBinding.inflate(inflater, container, false)
        initUi()
        return binding.root
    }

    private fun initUi() {
        val savedStartTime = preferenceManger.getStartTime()
        if (savedStartTime != 0L) {
            updateUi(savedStartTime!!)
        }

        binding.llMain.btnSave.setOnClickListener {
            if (validate()) {
                scheduleTime()
            }
        }
    }

    private fun updateUi(sheduledstartTime: Long) {
        binding.llMain.llshedule.isVisible = false
        binding.llMain.llstart.isVisible = true
        timer = object : CountDownTimer(sheduledstartTime - System.currentTimeMillis(), 1000) {
            override fun onTick(p0: Long) {
                val numberFormat = DecimalFormat("00")
                val hour = (p0 / 3600000) % 24
                val min = (p0 / 60000) % 60
                val sec = (p0 / 1000) % 60
                binding.llMain.tvTimer.text =
                    (numberFormat.format(hour) + ":" + numberFormat.format(min)).toString() + ":" + numberFormat.format(
                        sec
                    )
            }

            override fun onFinish() {
                preferenceManger.setLastTime(0)
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, FlagsChallemgeFragment())?.commit()
                //findNavController().navigate(R.id.action_sheduleChalengeFragment_to_flagsChallemgeFragment)
            }
        }
        timer?.start()
    }

        private fun validate():Boolean
    {
        var isValid=false
        if(binding.llMain.etH1.text.toString().trim().isEmpty())
        {
            Toast.makeText(activity,"Please enter a valid hour",Toast.LENGTH_SHORT).show()
        }
        else if(binding.llMain.etH2.text.toString().trim().isEmpty())
        {
            Toast.makeText(activity,"Please enter a valid hour",Toast.LENGTH_SHORT).show()
        }
        else if(binding.llMain.etM1.text.toString().trim().isEmpty())
        {
            Toast.makeText(activity,"Please enter a valid minute",Toast.LENGTH_SHORT).show()
        }
        else if(binding.llMain.etM2.text.toString().trim().isEmpty())
        {
            Toast.makeText(activity,"Please enter a valid minute",Toast.LENGTH_SHORT).show()
        }
        else if(binding.llMain.etS1.text.toString().trim().isEmpty())
        {
            Toast.makeText(activity,"Please enter a valid second",Toast.LENGTH_SHORT).show()
        }
        else if(binding.llMain.etS2.text.toString().trim().isEmpty())
        {
            Toast.makeText(activity,"Please enter a valid second",Toast.LENGTH_SHORT).show()
        }
        else
        {
            isValid=true
        }
        return isValid
    }

    private fun scheduleTime() {
        val h1= binding.llMain.etH1.text.toString().trim()
        val h2= binding.llMain.etH2.text.toString().trim()
        val m1= binding.llMain.etM1.text.toString().trim()
        val m2= binding.llMain.etM2.text.toString().trim()
        val s1= binding.llMain.etS1.text.toString().trim()
        val s2= binding.llMain.etS2.text.toString().trim()
        val hour= (h1 + h2).toInt()
        val minute= (m1 + m2).toInt()
        val second= (s1 + s2).toInt()
        val startSwithin="$hour:$minute:$second"
        val sheduledstartTime = parseTimeToMillis(startSwithin)
        preferenceManger.setStartTime(sheduledstartTime)
        updateUi(sheduledstartTime)
    }

    private fun parseTimeToMillis(timeStr: String): Long {
        try {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val date: Date = sdf.parse(timeStr)!!
            val targetTimeMillis: Long = date.time
            val currentTimeMillis = System.currentTimeMillis()
            val elapsedTime: Long = targetTimeMillis - sdf.parse("00:00:00")!!.time
            return currentTimeMillis + elapsedTime
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }
}