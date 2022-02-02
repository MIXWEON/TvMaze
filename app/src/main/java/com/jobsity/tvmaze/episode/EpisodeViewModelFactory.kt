package com.jobsity.tvmaze.episode

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jobsity.tvmaze.network.MazeEpisode

/**
 * Simple ViewModel factory that provides the MazeSerie and context to the ViewModel.
 */
class EpisodeViewModelFactory(
    private val mazeEpisode: MazeEpisode,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpisodeViewModel::class.java)) {
            return EpisodeViewModel(mazeEpisode, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
