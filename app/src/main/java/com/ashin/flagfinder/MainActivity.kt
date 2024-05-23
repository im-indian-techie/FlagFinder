package com.ashin.flagfinder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ashin.flagfinder.databinding.ActivityMainBinding
import com.ashin.flagfinder.model.Questions
import com.ashin.flagfinder.model.QustionAnswers
import com.ashin.flagfinder.utils.AppUtil
import com.ashin.flagfinder.utils.PreferenceManger
import com.ashin.flagfinder.view.fragment.FinalFragment
import com.ashin.flagfinder.view.fragment.FlagsChallemgeFragment
import com.ashin.flagfinder.view.fragment.SheduleChalengeFragment
import com.ashin.flagfinder.viewmodel.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val activity=this
    private lateinit var binding:ActivityMainBinding
    private val viewModel:MainViewModel by viewModels()
    private var list:ArrayList<Questions> = arrayListOf()
    private val questionDuration = 30000L
    private var ansDuration = 10000L
    @Inject
    lateinit var preferenceManger: PreferenceManger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.topAppBar)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
//        val navController = navHostFragment.navController
        //navController.navigate(R.id.sheduleChalengeFragment)
        val jsonData= AppUtil.readJsonFromAssets(activity,"flags.json")
        val qustionAnswers= Gson().fromJson(jsonData, QustionAnswers::class.java)
        list.addAll(qustionAnswers.questions)

        val currentTime = System.currentTimeMillis()
        val startTime= preferenceManger.getStartTime()
        val totalDuration = list.size * (questionDuration + ansDuration)

        when {
            startTime == 0L -> {
//                navController.navigate(R.id.sheduleChalengeFragment)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SheduleChalengeFragment())
                    .commit()

            }
            currentTime >= startTime!! + totalDuration -> {
//                navController.navigate(R.id.finalFragment)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, FinalFragment())
                    .commit()

            }
            currentTime >= startTime -> {
//                navController.navigate(R.id.flagsChallemgeFragment)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, FlagsChallemgeFragment())
                    .commit()

            }
            else->
            {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SheduleChalengeFragment())
                    .commit()
            }
        }


    }
    fun updateUiForStart()
    {
        viewModel.updateUI()
    }
}