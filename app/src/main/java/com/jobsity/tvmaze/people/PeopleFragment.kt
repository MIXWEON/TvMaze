package com.jobsity.tvmaze.people

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.databinding.FragmentPeopleBinding
import com.jobsity.tvmaze.network.MazePeople

/**
 * A simple [Fragment] subclass.
 * Use the [PeopleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PeopleFragment : Fragment(){
    /**
     * Lazily initialize our [PeopleViewModel].
     */
    private val viewModel: PeopleViewModel by lazy {
        ViewModelProvider(this).get(PeopleViewModel::class.java)
    }
    private lateinit var serieAdapter: PeopleGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPeopleBinding.inflate(inflater)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        serieAdapter = PeopleGridAdapter(PeopleGridAdapter.OnClickListener {
            viewModel.displayPersonDetails(it)
        })
        binding.peopleGrid.adapter = serieAdapter
        viewModel.navigateToSelectedPerson.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(PeopleFragmentDirections.actionShowPerson(it))
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

    private fun renderPhotosList(serieList: List<MazePeople>) {
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
                    viewModel.getMazePeoplesSorted(query!!)
                } else {
                    viewModel.getMazePeoplesSorted("")
                }
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (!query.equals("")) {
                    viewModel.getMazePeoplesSorted(query!!)
                } else {
                    viewModel.getMazePeoplesSorted("")
                }
                return true
            }
        })
    }
}