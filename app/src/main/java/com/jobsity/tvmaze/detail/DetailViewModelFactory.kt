package com.jobsity.tvmaze.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jobsity.tvmaze.database.FavoriteDao
import com.jobsity.tvmaze.network.MazeSerie

/**
 * Simple ViewModel factory that provides the MazeSerie and context to the ViewModel.
 */
class DetailViewModelFactory(
    private val mazeSerie: MazeSerie,
    private val application: Application,
    private val dataSource: FavoriteDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mazeSerie, application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
