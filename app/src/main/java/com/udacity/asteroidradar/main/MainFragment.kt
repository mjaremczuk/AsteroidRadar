package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.Factory(requireActivity().application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        binding.asteroidRecycler.adapter = AsteroidAdapter(OnClickListener {
            viewModel.displayDetails(it)
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            it?.let { asteroid ->
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.navigateToDetailComplete()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.filterAsteroids(
            when (item.itemId) {
                R.id.show_week_menu -> AsteroidRange.WEEK
                R.id.show_today_menu -> AsteroidRange.TODAY
                R.id.show_saved_menu -> AsteroidRange.SAVED
                else -> AsteroidRange.SAVED
            }
        )
        return true
    }
}
