package com.ashin.flagfinder.view.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.ashin.flagfinder.R
import com.ashin.flagfinder.databinding.FlagChallengeFragmentBinding
import com.ashin.flagfinder.model.Countries
import com.ashin.flagfinder.model.Questions
import com.ashin.flagfinder.model.QustionAnswers
import com.ashin.flagfinder.utils.AppUtil
import com.ashin.flagfinder.utils.PreferenceManger
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class FlagsChallemgeFragment:Fragment() {
    private lateinit var binding:FlagChallengeFragmentBinding
    private var countDownTimer:CountDownTimer?=null
    private val questionDuration = 30000L
    private var ansDuration = 10000L
    private var currentQuesIndex=0
    private var answeredId:Int?=null
    private var isClickedOn:String?=null
    private var list:ArrayList<Questions> = arrayListOf()
    private var timeLeftInMillis: Long = questionDuration
    private var timeLeftInMillisAns: Long = ansDuration
    lateinit var drawables: List<Drawable>
    @Inject
    lateinit var preferenceManger: PreferenceManger
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FlagChallengeFragmentBinding.inflate(inflater,container,false)
        initUi()
        return binding.root
    }

    private fun initUi() {
       val jsonData= AppUtil.readJsonFromAssets(requireContext(),"flags.json")
       val qustionAnswers= Gson().fromJson(jsonData,QustionAnswers::class.java)
       list.addAll(qustionAnswers.questions)
       drawables=AppUtil.getAllDrawables(requireActivity())
        restoreState()
    }
    private fun restoreState() {
        val savedTime = preferenceManger.getStartTime()

        if (savedTime == 0L) {
            currentQuesIndex = 0
            setQuestionUi()
        } else {
            val elapsedTime= System.currentTimeMillis()-savedTime!!
            val totalDuration= list.size * (questionDuration+ansDuration)

            if (elapsedTime >= totalDuration) {
//                findNavController().navigate(R.id.action_flagsChallemgeFragment_to_finalFragment)
                 activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, FinalFragment())?.commit()
            } else {
                currentQuesIndex= (elapsedTime/(questionDuration+ansDuration)).toInt()
                val cycleElapsedTime= elapsedTime%(questionDuration+ansDuration)

                if (cycleElapsedTime >= questionDuration) {
                    timeLeftInMillisAns=ansDuration-(cycleElapsedTime-questionDuration)
                    setAnswerUi()
                } else {
                    timeLeftInMillis=questionDuration-cycleElapsedTime
                    setQuestionUi()
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }
    private fun startTimerForQues(){
        countDownTimer= object : CountDownTimer(timeLeftInMillis,1000)
        {
            override fun onTick(p0: Long) {
                val numberFormat= DecimalFormat("00")
//                val hour= (p0 / 3600000) % 24
//                val min= (p0 / 60000) % 60
                val sec= (p0 / 1000) % 60
                binding.llmain.tvQTimer.text = (numberFormat.format(sec))
            }

            override fun onFinish() {
               //startTimerForAnswer()
               setAnswerUi()
            }

        }.start()
    }

    fun setAnswerUi()
    {
        isClickedOn=preferenceManger.getClickedOn()
        answeredId=preferenceManger.getAnseweredId()
        startTimerForAnswer()
        val question= list[currentQuesIndex]
        val ansIndex=getCorrectAnswerIndex(question.answerId,question.countries)
        preferenceManger.setTotalScore(list.size)
        binding.llmain.tvQno.text=(currentQuesIndex+1).toString()
        binding.llmain.tvAnsA.text=question.countries[0].countryName
        binding.llmain.tvAnsB.text=question.countries[1].countryName
        binding.llmain.tvAnsC.text=question.countries[2].countryName
        binding.llmain.tvAnsD.text=question.countries[3].countryName
        val flagImage= AppUtil.getDrawableByName(requireActivity(),question.countryCode!!.toLowerCase()!!)
        if(flagImage!=null)
        {
            Glide.with(requireActivity()).load(flagImage).into(binding.llmain.ivFlag)
        }

        //
        when(ansIndex)
        {
            0->{
                binding.llmain.tvAnsA.background= resources.getDrawable(R.drawable.correct)
                binding.llmain.tvAnsAst.text="correct"
            }
            1->{
                binding.llmain.tvAnsB.background= resources.getDrawable(R.drawable.correct)
                binding.llmain.tvAnsBst.text="correct"
            }
            2->{
                binding.llmain.tvAnsC.background= resources.getDrawable(R.drawable.correct)
                binding.llmain.tvAnsCst.text="correct"
            }
            3->{
                binding.llmain.tvAnsD.background= resources.getDrawable(R.drawable.correct)
                binding.llmain.tvAnsDst.text="correct"
            }
        }
        if(isClickedOn.equals("A"))
        {
            if(answeredId==question.answerId)
            {
                Log.d("test","test")
                binding.llmain.tvAnsA.background= resources.getDrawable(R.drawable.correct)
                binding.llmain.tvAnsAst.text="correct"
                preferenceManger.setScore(preferenceManger.getScore()!!+1)

            }
            else {
                binding.llmain.tvAnsA.background = resources.getDrawable(R.drawable.fail)
                binding.llmain.tvAnsAst.text="wrong"
            }
            binding.llmain.tvAnsB.isEnabled=false
            binding.llmain.tvAnsC.isEnabled=false
            binding.llmain.tvAnsD.isEnabled=false
        }
        if(isClickedOn.equals("B"))
        {
            if(answeredId==question.answerId)
            {
                binding.llmain.tvAnsB.background= resources.getDrawable(R.drawable.correct)
                binding.llmain.tvAnsBst.text="correct"
                preferenceManger.setScore(preferenceManger.getScore()!!+1)
            }
            else {
                binding.llmain.tvAnsB.background = resources.getDrawable(R.drawable.fail)
                binding.llmain.tvAnsBst.text="wrong"
            }
            binding.llmain.tvAnsA.isEnabled=false
            binding.llmain.tvAnsC.isEnabled=false
            binding.llmain.tvAnsD.isEnabled=false
        }
        if(isClickedOn.equals("C"))
        {
            if(answeredId==question.answerId)
            {
                binding.llmain.tvAnsC.background= resources.getDrawable(R.drawable.correct)
                binding.llmain.tvAnsCst.text="correct"
                preferenceManger.setScore(preferenceManger.getScore()!!+1)
            }
            else {
                binding.llmain.tvAnsC.background = resources.getDrawable(R.drawable.fail)
                binding.llmain.tvAnsCst.text="wrong"
            }
            binding.llmain.tvAnsA.isEnabled=false
            binding.llmain.tvAnsB.isEnabled=false
            binding.llmain.tvAnsD.isEnabled=false
        }
        if(isClickedOn.equals("D"))
        {
            if(answeredId==question.answerId)
            {
                binding.llmain.tvAnsD.background= resources.getDrawable(R.drawable.correct)
                binding.llmain.tvAnsDst.text="correct"
                preferenceManger.setScore(preferenceManger.getScore()!!+1)
            }
            else {
                binding.llmain.tvAnsD.background = resources.getDrawable(R.drawable.fail)
                binding.llmain.tvAnsDst.text="wrong"
            }
            binding.llmain.tvAnsA.isEnabled=false
            binding.llmain.tvAnsB.isEnabled=false
            binding.llmain.tvAnsC.isEnabled=false
        }

    }
    private fun startTimerForAnswer(){
        countDownTimer= object : CountDownTimer(timeLeftInMillisAns,1000)
        {
            override fun onTick(p0: Long) {
                val numberFormat= DecimalFormat("00")
//                val hour= (p0 / 3600000) % 24
//                val min= (p0 / 60000) % 60
                val sec= (p0 / 1000) % 60
                binding.llmain.tvQTimer.text = (numberFormat.format(sec))
            }

            override fun onFinish() {
                preferenceManger.setClickedOn("")
                preferenceManger.setAnseweredId(0)
                 resetUi()
                currentQuesIndex++
                Log.d("pos1",currentQuesIndex.toString())
                 if(currentQuesIndex<list.size) {
                     Log.d("pos2",currentQuesIndex.toString())
                     setQuestionUi()
                 }
                 else
                 {
//                     findNavController().navigate(R.id.action_flagsChallemgeFragment_to_finalFragment)
                     activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, FinalFragment())?.commit()
                     Log.d("pos3",currentQuesIndex.toString())
                 }

            }

        }.start()
    }
    fun resetUi()
    {
        binding.llmain.tvQno.text=""
        binding.llmain.tvAnsA.text=""
        binding.llmain.tvAnsB.text=""
        binding.llmain.tvAnsC.text=""
        binding.llmain.tvAnsD.text=""
        //
        binding.llmain.tvAnsAst.text=""
        binding.llmain.tvAnsBst.text=""
        binding.llmain.tvAnsCst.text=""
        binding.llmain.tvAnsDst.text=""

        binding.llmain.tvAnsA.background= resources.getDrawable(R.drawable.rectangle)
        binding.llmain.tvAnsB.background= resources.getDrawable(R.drawable.rectangle)
        binding.llmain.tvAnsC.background= resources.getDrawable(R.drawable.rectangle)
        binding.llmain.tvAnsD.background= resources.getDrawable(R.drawable.rectangle)
        //
        binding.llmain.tvAnsA.isEnabled=true
        binding.llmain.tvAnsB.isEnabled=true
        binding.llmain.tvAnsC.isEnabled=true
        binding.llmain.tvAnsD.isEnabled=true
    }
    private fun setQuestionUi()
    {
        isClickedOn=preferenceManger.getClickedOn()
        answeredId=preferenceManger.getAnseweredId()
        startTimerForQues()
        val question= list[currentQuesIndex]
        binding.llmain.tvQno.text= (currentQuesIndex+1).toString()
        binding.llmain.tvAnsA.text=question.countries[0].countryName
        binding.llmain.tvAnsB.text=question.countries[1].countryName
        binding.llmain.tvAnsC.text=question.countries[2].countryName
        binding.llmain.tvAnsD.text=question.countries[3].countryName
        val flagImage= AppUtil.getDrawableByName(requireActivity(),question.countryCode!!.toLowerCase()!!)
        if(flagImage!=null)
        {
            Glide.with(requireActivity()).load(flagImage).into(binding.llmain.ivFlag)
        }
        if(isClickedOn.equals("A"))
        {
            binding.llmain.tvAnsA.background= resources.getDrawable(R.drawable.fail)
            binding.llmain.tvAnsB.isEnabled=false
            binding.llmain.tvAnsC.isEnabled=false
            binding.llmain.tvAnsD.isEnabled=false
            preferenceManger.setAnseweredId(question.countries[0].id)
            answeredId=preferenceManger.getAnseweredId()
            preferenceManger.setClickedOn("A")
            isClickedOn=preferenceManger.getClickedOn()
        }
        binding.llmain.tvAnsA.setOnClickListener{
             binding.llmain.tvAnsA.background= resources.getDrawable(R.drawable.fail)
             binding.llmain.tvAnsB.isEnabled=false
             binding.llmain.tvAnsC.isEnabled=false
             binding.llmain.tvAnsD.isEnabled=false
             preferenceManger.setAnseweredId(question.countries[0].id)
             answeredId=preferenceManger.getAnseweredId()
             preferenceManger.setClickedOn("A")
             isClickedOn=preferenceManger.getClickedOn()
        }
        if(isClickedOn.equals("B"))
        {
            binding.llmain.tvAnsB.background= resources.getDrawable(R.drawable.fail)
            binding.llmain.tvAnsA.isEnabled=false
            binding.llmain.tvAnsC.isEnabled=false
            binding.llmain.tvAnsD.isEnabled=false
            preferenceManger.setAnseweredId(question.countries[1].id)
            answeredId=preferenceManger.getAnseweredId()
            preferenceManger.setClickedOn("B")
            isClickedOn=preferenceManger.getClickedOn()
        }
        binding.llmain.tvAnsB.setOnClickListener{
            binding.llmain.tvAnsB.background= resources.getDrawable(R.drawable.fail)
            binding.llmain.tvAnsA.isEnabled=false
            binding.llmain.tvAnsC.isEnabled=false
            binding.llmain.tvAnsD.isEnabled=false
            preferenceManger.setAnseweredId(question.countries[1].id)
            answeredId=preferenceManger.getAnseweredId()
            preferenceManger.setClickedOn("B")
            isClickedOn=preferenceManger.getClickedOn()
        }
        if(isClickedOn.equals("C"))
        {
            binding.llmain.tvAnsC.background= resources.getDrawable(R.drawable.fail)
            binding.llmain.tvAnsA.isEnabled=false
            binding.llmain.tvAnsB.isEnabled=false
            binding.llmain.tvAnsD.isEnabled=false
            preferenceManger.setAnseweredId(question.countries[2].id)
            answeredId=preferenceManger.getAnseweredId()
            preferenceManger.setClickedOn("C")
            isClickedOn=preferenceManger.getClickedOn()
        }
        binding.llmain.tvAnsC.setOnClickListener{
            binding.llmain.tvAnsC.background= resources.getDrawable(R.drawable.fail)
            binding.llmain.tvAnsA.isEnabled=false
            binding.llmain.tvAnsB.isEnabled=false
            binding.llmain.tvAnsD.isEnabled=false
            preferenceManger.setAnseweredId(question.countries[2].id)
            answeredId=preferenceManger.getAnseweredId()
            preferenceManger.setClickedOn("C")
            isClickedOn=preferenceManger.getClickedOn()
        }
        if(isClickedOn.equals("D"))
        {
            binding.llmain.tvAnsD.background= resources.getDrawable(R.drawable.fail)
            binding.llmain.tvAnsB.isEnabled=false
            binding.llmain.tvAnsC.isEnabled=false
            binding.llmain.tvAnsA.isEnabled=false
            preferenceManger.setAnseweredId(question.countries[3].id)
            answeredId=preferenceManger.getAnseweredId()
            preferenceManger.setClickedOn("D")
            isClickedOn=preferenceManger.getClickedOn()
        }
        binding.llmain.tvAnsD.setOnClickListener{
            binding.llmain.tvAnsD.background= resources.getDrawable(R.drawable.fail)
            binding.llmain.tvAnsB.isEnabled=false
            binding.llmain.tvAnsC.isEnabled=false
            binding.llmain.tvAnsA.isEnabled=false
            preferenceManger.setAnseweredId(question.countries[3].id)
            answeredId=preferenceManger.getAnseweredId()
            preferenceManger.setClickedOn("D")
            isClickedOn=preferenceManger.getClickedOn()
        }

    }
    fun getCorrectAnswerIndex(answerId: Int?, countries: ArrayList<Countries>): Int?
    {
        var id: Int? =null
        for (i in 0 until countries.size)
        {
            val item=countries[i]
            if(item.id==answerId)
            {
                id= i
            }
        }
        return id
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onStop() {
        super.onStop()
        preferenceManger.setLastIndex(currentQuesIndex)
        preferenceManger.setLastTime(System.currentTimeMillis())
    }



}