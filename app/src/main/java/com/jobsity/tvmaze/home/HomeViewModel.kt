package com.jobsity.tvmaze.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobsity.tvmaze.network.MazeApi
import com.jobsity.tvmaze.network.MazeSerie
import com.jobsity.tvmaze.network.Show
import kotlinx.coroutines.launch

enum class MazeApiStatus { LOADING, ERROR, DONE }
class HomeViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<MazeApiStatus>()

    // The external immutable LiveData for the response String
    val status: LiveData<MazeApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MazeSerie>>()

    val properties: LiveData<List<MazeSerie>>
        get() = _properties

    private val _show = MutableLiveData<List<Show>>()

    val show: LiveData<List<Show>>
        get() = _show

    private val _navigateToSelectedSerie = MutableLiveData<MazeSerie>()

    val navigateToSelectedSerie: LiveData<MazeSerie>
        get() = _navigateToSelectedSerie

    fun displaySerieDetails(mazeSerie: MazeSerie) {
        _navigateToSelectedSerie.value = mazeSerie
    }

    fun displaySerieDetailComplete() {
        _navigateToSelectedSerie.value = null
    }

    /**
     * Call getMazeSeriesInitialize() on init so we can display series immediately.
     */
    init {
        getMazeSeriesSorted("cars")
    }

    /**
     * Sets the value of the search LiveData to the Maze API Series.
     */
    fun getMazeSeriesSorted(filter: String) {
        viewModelScope.launch {
            _status.value = MazeApiStatus.LOADING
            try {
                _properties.value = MazeApi.retrofitService.getProperties(filter)
                _status.value = MazeApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MazeApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    fun getMazeSeriesInitialize() {
        viewModelScope.launch {
            _status.value = MazeApiStatus.LOADING
            try {
                _show.value = MazeApi.retrofitService.getShows()
                _status.value = MazeApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MazeApiStatus.ERROR
                _show.value = ArrayList()
            }
        }
    }

    fun updateFilter(filter: String) {
        getMazeSeriesSorted(filter)
    }
}