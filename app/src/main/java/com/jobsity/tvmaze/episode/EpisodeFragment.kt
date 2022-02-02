package com.jobsity.tvmaze.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jobsity.tvmaze.databinding.FragmentEpisodeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EpisodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EpisodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        val binding = FragmentEpisodeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val mazeEpisode = EpisodeFragmentArgs.fromBundle(requireArguments()).selectedEpisode
        val viewModelFactory = EpisodeViewModelFactory(mazeEpisode, application)
        binding.viewModel =
            ViewModelProvider(this, viewModelFactory).get(EpisodeViewModel::class.java)
        return binding.root
    }

}