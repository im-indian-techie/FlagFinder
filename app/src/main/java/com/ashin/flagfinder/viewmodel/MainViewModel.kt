package com.ashin.flagfinder.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashin.flagfinder.FlagApplication
import com.ashin.flagfinder.model.Questions
import com.ashin.flagfinder.model.QustionAnswers
import com.ashin.flagfinder.utils.AppUtil
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor():ViewModel() {
    @Inject lateinit var application: FlagApplication

    private val updateUI=MutableLiveData<Boolean>()
    val _updateUI:LiveData<Boolean> = updateUI

    private val questionlist=MutableLiveData<ArrayList<Questions>>()
    val  _questionlist:LiveData<ArrayList<Questions>> = questionlist
    fun updateUI() {
        updateUI.value=true
    }

    fun getList()
    {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonData= AppUtil.readJsonFromAssets(application.applicationContext,"flags.json")
            val qustionAnswers= Gson().fromJson(jsonData, QustionAnswers::class.java)
            if(qustionAnswers!=null)
            {
                withContext(Dispatchers.Main)
                {
                    questionlist.value=qustionAnswers.questions
                }

            }
        }

    }

}