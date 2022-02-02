package com.jobsity.tvmaze.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobsity.tvmaze.network.MazeApi
import com.jobsity.tvmaze.network.MazePeople
import kotlinx.coroutines.launch

enum class MazePeopleStatus { LOADING, ERROR, DONE }
class PeopleViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<MazePeopleStatus>()

    // The external immutable LiveData for the response String
    val status: LiveData<MazePeopleStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MazePeople>>()

    val properties: LiveData<List<MazePeople>>
        get() = _properties

    private val _navigateToSelectedPerson = MutableLiveData<MazePeople>()

    val navigateToSelectedPerson: LiveData<MazePeople>
        get() = _navigateToSelectedPerson

    fun displayPersonDetails(mazeSerie: MazePeople) {
        _navigateToSelectedPerson.value = mazeSerie
    }

    fun displaySerieDetailComplete() {
        _navigateToSelectedPerson.value = null
    }

    /**
     * Sets the value of the search LiveData to the Maze API Series.
     */
    fun getMazePeoplesSorted(filter: String) {
        viewModelScope.launch {
            _status.value = MazePeopleStatus.LOADING
            try {
                _properties.value = MazeApi.retrofitService.getPeople(filter)
                _status.value = MazePeopleStatus.DONE
            } catch (e: Exception) {
                _status.value = MazePeopleStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }
}