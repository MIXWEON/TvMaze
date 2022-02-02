package com.jobsity.tvmaze.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.databinding.FragmentHomeBinding
import com.jobsity.tvmaze.network.MazeSerie

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(){
    /**
     * Lazily initialize our [HomeViewModel].
     */
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }
    private lateinit var serieAdapter: PhotoGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        serieAdapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displaySerieDetails(it)
        })
        binding.photosGrid.adapter = serieAdapter
        viewModel.navigateToSelectedSerie.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(HomeFragmentDirections.actionShowDetail(it))
                viewModel.displaySerieDetailComplete()
            }
        })
        viewModel.properties.observe(viewLifecycleOwner, Observer {
            renderPhotosList(it!!)
        })
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun renderPhotosList(serieList: List<MazeSerie>) {
        serieAdapter.addData(serieList)
        serieAdapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuItem = menu.findItem(R.id.search_serie)
        val search = menuItem.actionView as SearchView
        searching(search)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_serie -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searching(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.equals("") || query != null) {
                    viewModel.getMazeSeriesSorted(query!!)
                } else {
                    viewModel.getMazeSeriesSorted("cars")
                }
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (!query.equals("")) {
                    viewModel.getMazeSeriesSorted(query!!)
                } else {
                    viewModel.getMazeSeriesSorted("cars")
                }
                return true
            }
        })
    }
}