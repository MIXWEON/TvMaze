package com.jobsity.tvmaze.favorite

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.database.FavoriteDatabase
import com.jobsity.tvmaze.databinding.FragmentFavoriteBinding
import com.jobsity.tvmaze.databinding.FragmentHomeBinding
import com.jobsity.tvmaze.network.MazeSerie

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment(){

    private lateinit var serieAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDao()
        val viewModelFactory = FavoriteViewModelFactory(dataSource!!, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)
        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel
        serieAdapter = FavoriteAdapter(FavoriteAdapter.OnClickListener {
            viewModel.displaySerieDetails(it)
        })
        binding.favoriteGrid.adapter = serieAdapter
        viewModel.navigateToSelectedFavorite.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(FavoriteFragmentDirections.actionShowDetail(it))
                viewModel.displaySerieDetailComplete()
            }
        })
        viewModel.properties.observe(viewLifecycleOwner, Observer {
            renderPhotosList(it!!)
        })
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun renderPhotosList(serieList: List<MazeSerie>) {
        serieAdapter.addData(serieList)
        serieAdapter.notifyDataSetChanged()

    }

}