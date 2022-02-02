package com.jobsity.tvmaze.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobsity.tvmaze.database.FavoriteDao
import com.jobsity.tvmaze.network.*
import kotlinx.coroutines.launch

enum class MazeFavoriteStatus { LOADING, ERROR, DONE }
class FavoriteViewModel(
    val database: FavoriteDao,
    application: Application
) : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<MazeFavoriteStatus>()

    // The external immutable LiveData for the response String
    val status: LiveData<MazeFavoriteStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MazeSerie>>()

    val properties: LiveData<List<MazeSerie>>
        get() = _properties

    private val _show = MutableLiveData<List<Show>>()

    val show: LiveData<List<Show>>
        get() = _show

    private val _navigateToSelectedFavorite = MutableLiveData<MazeSerie>()

    val navigateToSelectedFavorite: LiveData<MazeSerie>
        get() = _navigateToSelectedFavorite

    fun displaySerieDetails(mazeSerie: MazeSerie) {
        _navigateToSelectedFavorite.value = mazeSerie
    }

    fun displaySerieDetailComplete() {
        _navigateToSelectedFavorite.value = null
    }

    /**
     * Call getMazeSeriesInitialize() on init so we can display series immediately.
     */
    init {
        getMazeSeriesSorted()
    }

    /**
     * Sets the value of the search LiveData to the Maze API Series.
     */
    fun getMazeSeriesSorted() {
        viewModelScope.launch {
            _status.value = MazeFavoriteStatus.LOADING
            try {
                val favorites = database.favoriteData()
                val favoritesList: MutableList<MazeSerie> = mutableListOf()
                if (favorites != null) {
                    for(serie in favorites) {
                        var serieSchedule = serie?.scheduleTime?.let { serie.scheduleDays?.let { it1 ->
                            Schedule(it,
                                it1
                            )
                        } }
                        var serieImages = serie?.imageMedium?.let { serie.imageOriginal?.let { it1 ->
                            ImageType(it,
                                it1
                            )
                        } }
                        val show: Show? = serie?.let { serie.name?.let { it1 ->
                            serie.type?.let { it2 ->
                                serie.language?.let { it3 ->
                                    serieSchedule?.let { it4 ->
                                        serie.genres?.let { it5 ->
                                            Show(it.id,
                                                it1,
                                                it2, it3, serieImages, it4, it5, serie.summary)
                                        }
                                    }
                                }
                            }
                        } }
                        var maze: MazeSerie? = show?.let { MazeSerie(null, it) }
                        if (maze != null) {
                            favoritesList.add(maze)
                        }
                    }
                }
                _properties.value = favoritesList
                _status.value = MazeFavoriteStatus.DONE
            } catch (e: Exception) {
                _status.value = MazeFavoriteStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }
}