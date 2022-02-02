package com.jobsity.tvmaze.person

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jobsity.tvmaze.network.MazePeople

/**
 * Simple ViewModel factory that provides the MazeSerie and context to the ViewModel.
 */
class PersonViewModelFactory(
    private val mazePeople: MazePeople,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            return PersonViewModel(mazePeople, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
