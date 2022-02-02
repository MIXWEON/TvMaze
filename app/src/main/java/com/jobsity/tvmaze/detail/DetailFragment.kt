package com.jobsity.tvmaze.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jobsity.tvmaze.database.FavoriteDatabase
import com.jobsity.tvmaze.databinding.FragmentDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    private lateinit var serieAdapter: SerieGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val mazeSerie = DetailFragmentArgs.fromBundle(requireArguments()).selectedSerie
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDao()
        val viewModelFactory = DetailViewModelFactory(mazeSerie, application, dataSource!!)
        val viewModelDetail: DetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        serieAdapter = SerieGridAdapter(SerieGridAdapter.OnClickListener {
            viewModelDetail.displayEpisodeDetails(it)
        })
        binding.episodesGrid.adapter = serieAdapter
        viewModelDetail.episodes.observe(viewLifecycleOwner, Observer {
            it?.let {
                serieAdapter.addHeaderAndSumbitList(it)
            }
        })
        viewModelDetail.navigateToSelectedEpisode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(DetailFragmentDirections.actionShowEpisode(it))
                viewModelDetail.displayEpisodeDetailComplete()
            }
        })

        binding.viewModel = viewModelDetail
        return binding.root
    }
}