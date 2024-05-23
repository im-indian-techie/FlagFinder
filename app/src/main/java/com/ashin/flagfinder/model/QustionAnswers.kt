package com.ashin.flagfinder.model

import com.google.gson.annotations.SerializedName


data class QustionAnswers (
  @SerializedName("questions" ) var questions : ArrayList<Questions> = arrayListOf()

)