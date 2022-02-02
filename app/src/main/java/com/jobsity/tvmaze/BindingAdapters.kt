package com.jobsity.tvmaze

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jobsity.tvmaze.detail.SerieGridAdapter
import com.jobsity.tvmaze.favorite.FavoriteAdapter
import com.jobsity.tvmaze.favorite.MazeFavoriteStatus
import com.jobsity.tvmaze.home.MazeApiStatus
import com.jobsity.tvmaze.home.PhotoGridAdapter
import com.jobsity.tvmaze.network.MazeEpisode
import com.jobsity.tvmaze.network.MazePeople
import com.jobsity.tvmaze.network.MazeSerie
import com.jobsity.tvmaze.people.MazePeopleStatus
import com.jobsity.tvmaze.people.PeopleGridAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl.let {
        val imgUri = imgUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions().placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .fallback(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MazeSerie>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    if (data != null) {
        adapter.addData(data)
    }
}

@BindingAdapter("listFavoriteData")
fun bindFavorite(recyclerView: RecyclerView, data: List<MazeSerie>?) {
    val adapter = recyclerView.adapter as FavoriteAdapter
    if (data != null) {
        adapter.addData(data)
    }
}

@BindingAdapter("episodeData")
fun bindEpisodes(recyclerView: RecyclerView, data: List<MazeEpisode>?) {
    val adapter = recyclerView.adapter as SerieGridAdapter
    if (data != null) {
        adapter.addData(data)
    }
}

@BindingAdapter("peopleData")
fun bindPeople(recyclerView: RecyclerView, data: List<MazePeople>?) {
    val adapter = recyclerView.adapter as PeopleGridAdapter
    if (data != null) {
        adapter.addData(data)
    }
}

@BindingAdapter("mazeApiStatus")
fun bindStatus(statusImageView: ImageView, status: MazeApiStatus?) {
    when (status) {
        MazeApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MazeApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MazeApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("mazePeopleStatus")
fun bindPeopleStatus(statusImageView: ImageView, status: MazePeopleStatus?) {
    when (status) {
        MazePeopleStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MazePeopleStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MazePeopleStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("mazeFavoriteStatus")
fun bindFavoriteStatus(statusImageView: ImageView, status: MazeFavoriteStatus?) {
    when (status) {
        MazePeopleStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MazePeopleStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MazePeopleStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}