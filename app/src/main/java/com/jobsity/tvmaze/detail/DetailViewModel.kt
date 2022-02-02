package com.jobsity.tvmaze.detail

import android.app.Application
import android.os.Build
import android.text.Html
import androidx.lifecycle.*
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.database.FavoriteDao
import com.jobsity.tvmaze.database.FavoriteList
import com.jobsity.tvmaze.home.MazeApiStatus
import com.jobsity.tvmaze.network.MazeApi
import com.jobsity.tvmaze.network.MazeEpisode
import com.jobsity.tvmaze.network.MazeSerie
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel(mazeSerie: MazeSerie, app: Application, val database: FavoriteDao) :
    AndroidViewModel(app) {
    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<MazeApiStatus>()

    // The external immutable LiveData for the response String
    val status: LiveData<MazeApiStatus>
        get() = _status

    private val _episodes = MutableLiveData<List<MazeEpisode>>()

    val episodes: LiveData<List<MazeEpisode>>
        get() = _episodes
    private val _selectedSerie = MutableLiveData<MazeSerie>()
    val selectedSerie: LiveData<MazeSerie>
        get() = _selectedSerie

    private val _navigateToSelectedEpisode = MutableLiveData<MazeEpisode>()

    val navigateToSelectedEpisode: LiveData<MazeEpisode>
        get() = _navigateToSelectedEpisode

    init {
        _selectedSerie.value = mazeSerie
        getMazeSeriesEpisodes(mazeSerie.show.id.toString())
    }

    val displaySerieName = Transformations.map(selectedSerie) {
        if (!it.show.name.equals("")) {
            app.applicationContext.getString(R.string.display_name, it.show.name)
        } else {
            app.applicationContext.getString(R.string.display_name, "No name")
        }
    }

    val displaySerieDays = Transformations.map(selectedSerie) {
        if (it.show.schedule.days.isNotEmpty()) {
            val daysString = it.show.schedule.days.joinToString(", ")
            app.applicationContext.getString(R.string.display_days, daysString)
        } else {
            app.applicationContext.getString(R.string.display_days, "No days")
        }
    }

    val displaySerieTime = Transformations.map(selectedSerie) {
        if (!it.show.schedule.time.equals("")) {
            app.applicationContext.getString(R.string.display_time, it.show.schedule.time)
        } else {
            app.applicationContext.getString(R.string.display_time, "No time")
        }
    }
    val displaySerieGenres = Transformations.map(selectedSerie) {
        if (it.show.genres.isNotEmpty()) {
            val genresString = it.show.genres.joinToString(", ")
            app.applicationContext.getString(R.string.display_genre, genresString)
        } else {
            app.applicationContext.getString(R.string.display_genre, "No genres")
        }
    }
    val displaySerieSummary = Transformations.map(selectedSerie) {
        if (it.show.summary != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                app.applicationContext.getString(
                    R.string.display_summary,
                    Html.fromHtml(it.show.summary, Html.FROM_HTML_MODE_COMPACT)
                )
            } else {
                app.applicationContext.getString(
                    R.string.display_summary,
                    Html.fromHtml(it.show.summary)
                )
            }
        } else {
            app.applicationContext.getString(R.string.display_summary, "No Summary")
        }
    }
    val displaySerieFavorite = Transformations.map(selectedSerie) {

        database.isFavorite(it.show.id) == 1

    }

    fun displayEpisodeDetails(mazeEpisode: MazeEpisode) {
        _navigateToSelectedEpisode.value = mazeEpisode
    }

    fun displayEpisodeDetailComplete() {
        _navigateToSelectedEpisode.value = null
    }

    /**
     * Sets the value of the search LiveData to the Maze API Series.
     */
    fun getMazeSeriesEpisodes(id: String) {
        viewModelScope.launch {
            _status.value = MazeApiStatus.LOADING
            try {
                _episodes.value = MazeApi.retrofitService.getEpisodes(id)
                _status.value = MazeApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MazeApiStatus.ERROR
                _episodes.value = ArrayList()
            }
        }
    }

    /**
     * Executes when the START button is clicked.
     */
    fun onStartFavorite() {
        viewModelScope.launch {
            var favoriteList: FavoriteList = FavoriteList()
            favoriteList.id = _selectedSerie.value?.show?.id ?: 0
            favoriteList.name = _selectedSerie.value?.show?.name
            favoriteList.type = _selectedSerie.value?.show?.type
            favoriteList.language = _selectedSerie.value?.show?.language
            favoriteList.imageMedium = _selectedSerie.value?.show?.image?.medium
            favoriteList.imageOriginal = _selectedSerie.value?.show?.image?.original
            favoriteList.scheduleDays = _selectedSerie.value?.show?.schedule?.days
            favoriteList.scheduleTime = _selectedSerie.value?.show?.schedule?.time
            favoriteList.genres = _selectedSerie.value?.show?.genres
            favoriteList.summary = _selectedSerie.value?.show?.summary
            if (database.isFavorite(favoriteList.id) != 1) {
                database.addData(favoriteList)
            } else {
                database.delete(favoriteList)
            }
        }
    }
}