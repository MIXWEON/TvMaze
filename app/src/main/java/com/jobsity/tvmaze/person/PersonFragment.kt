package com.jobsity.tvmaze.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jobsity.tvmaze.databinding.FragmentPersonBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PersonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        val binding = FragmentPersonBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val mazePerson = PersonFragmentArgs.fromBundle(requireArguments()).selectedPerson
        val viewModelFactory = PersonViewModelFactory(mazePerson, application)
        val viewModelPerson: PersonViewModel =
            ViewModelProvider(this, viewModelFactory).get(PersonViewModel::class.java)

        binding.viewModel = viewModelPerson
        return binding.root
    }

}