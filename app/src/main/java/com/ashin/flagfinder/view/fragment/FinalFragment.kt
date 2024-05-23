package com.ashin.flagfinder.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ashin.flagfinder.R
import com.ashin.flagfinder.databinding.FinalFragmentBinding
import com.ashin.flagfinder.databinding.FlagChallengeFragmentBinding
import com.ashin.flagfinder.utils.PreferenceManger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FinalFragment : Fragment(){
    @Inject
    lateinit var preferenceManger: PreferenceManger
    private lateinit var binding: FinalFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FinalFragmentBinding.inflate(inflater,container,false)
        initUi()
        return binding.root
    }

    private fun initUi() {
        lifecycleScope.launch {
            delay(1000)
            binding.llFinal.tvGamerOver.isVisible=false
            binding.llFinal.llscore.isVisible=true
            val totalScore= preferenceManger.getTotalScore()
            val earnedScore=preferenceManger.getScore()
            binding.llFinal.tvScore.text= "$totalScore/$earnedScore"
            preferenceManger.clearPrefs()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }
}