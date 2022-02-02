package com.jobsity.tvmaze.person

import android.app.Application
import androidx.lifecycle.*
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.home.MazeApiStatus
import com.jobsity.tvmaze.network.MazeApi
import com.jobsity.tvmaze.network.MazeEpisode
import com.jobsity.tvmaze.network.MazePeople
import kotlinx.coroutines.launch
import java.lang.Exception

class PersonViewModel(mazeSerie: MazePeople, app: Application) : AndroidViewModel(app) {
    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<MazeApiStatus>()

    // The external immutable LiveData for the response String
    val status: LiveData<MazeApiStatus>
        get() = _status

    private val _shows = MutableLiveData<List<MazeEpisode>>()

    val shows: LiveData<List<MazeEpisode>>
        get() = _shows
    private val _selectedPerson = MutableLiveData<MazePeople>()
    val selectedPerson: LiveData<MazePeople>
        get() = _selectedPerson

    private val _navigateToSelectedShow = MutableLiveData<MazeEpisode>()

    val navigateToSelectedShow: LiveData<MazeEpisode>
        get() = _navigateToSelectedShow

    init {
        _selectedPerson.value = mazeSerie
        getMazePeoplesEpisodes(mazeSerie.person.id.toString())
    }

    val displayPersonName = Transformations.map(selectedPerson) {
        if (!it.person.name.equals("")) {
            app.applicationContext.getString(R.string.display_name, it.person.name)
        } else {
            app.applicationContext.getString(R.string.display_name, "No name")
        }
    }

    val displayPersonGenre = Transformations.map(selectedPerson) {
        if (it.person.genre.equals("")) {
            app.applicationContext.getString(R.string.display_days, it.person.genre)
        } else {
            app.applicationContext.getString(R.string.display_days, "No genre")
        }
    }

    fun displayPersonShows(mazeEpisode: MazeEpisode) {
        _navigateToSelectedShow.value = mazeEpisode
    }

    fun displaypersonShowComplete() {
        _navigateToSelectedShow.value = null
    }

    /**
     * Sets the value of the search LiveData to the Maze API Series.
     */
    fun getMazePeoplesEpisodes(id: String) {
        viewModelScope.launch {
            _status.value = MazeApiStatus.LOADING
            try {
                _shows.value = MazeApi.retrofitService.getEpisodes(id)
                _status.value = MazeApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MazeApiStatus.ERROR
                _shows.value = ArrayList()
            }
        }
    }
}