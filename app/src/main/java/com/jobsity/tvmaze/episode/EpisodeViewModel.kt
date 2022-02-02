package com.jobsity.tvmaze.episode

import android.app.Application
import android.os.Build
import android.text.Html
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.home.MazeApiStatus
import com.jobsity.tvmaze.network.MazeEpisode

class EpisodeViewModel(mazeEpisode: MazeEpisode, app: Application) : AndroidViewModel(app) {
    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<MazeApiStatus>()

    // The external immutable LiveData for the response String
    val status: LiveData<MazeApiStatus>
        get() = _status

    private val _selectedEpisode = MutableLiveData<MazeEpisode>()
    val selectedEpisode: LiveData<MazeEpisode>
        get() = _selectedEpisode

    private val _navigateToSelectedEpisode = MutableLiveData<MazeEpisode>()

    val navigateToSelectedEpisode: LiveData<MazeEpisode>
        get() = _navigateToSelectedEpisode

    init {
        _selectedEpisode.value = mazeEpisode
    }

    val displayEpisodeName = Transformations.map(selectedEpisode) {
        if (!it.name.equals("")) {
            app.applicationContext.getString(R.string.display_name, it.name)
        } else {
            app.applicationContext.getString(R.string.display_name, "No name")
        }
    }

    val displayEpisodeNumber = Transformations.map(selectedEpisode) {
        if (it.number > 0) {
            app.applicationContext.getString(R.string.display_number, it.number)
        } else {
            app.applicationContext.getString(R.string.display_name, "No number")
        }
    }

    val displayEpisodeSeason = Transformations.map(selectedEpisode) {
        if (it.season > 0) {
            app.applicationContext.getString(R.string.display_season, it.season)
        } else {
            app.applicationContext.getString(R.string.display_name, "No season")
        }
    }
    val displaySerieSummary = Transformations.map(selectedEpisode) {
        if (it.summary != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                app.applicationContext.getString(
                    R.string.display_summary,
                    Html.fromHtml(it.summary, Html.FROM_HTML_MODE_COMPACT)
                )
            } else {
                app.applicationContext.getString(
                    R.string.display_summary,
                    Html.fromHtml(it.summary)
                )
            }
        } else {
            app.applicationContext.getString(R.string.display_summary, "No Summary")
        }
    }
}