package com.github.edasich.place.finder.ui.favorit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.edasich.place.finder.ui.R
import com.github.edasich.place.finder.ui.databinding.FragmentFavoriteNearbyPlacesBinding
import com.github.edasich.place.finder.ui.nearby.view.NearbyPlaceListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteNearbyPlacesFragment : Fragment() {

    val viewModel: FavoriteNearbyPlacesViewModel by viewModels()

    lateinit var layout: FragmentFavoriteNearbyPlacesBinding

    private val favoriteNearbyPlaceListAdapter: NearbyPlaceListAdapter by lazy {
        NearbyPlaceListAdapter {
            viewModel.processRequest(
                request = FavoriteNearbyPlacesScreenRequest.OnFavoritePlaceClicked(
                    place = it
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentFavoriteNearbyPlacesBinding.inflate(inflater, container, false)
        initToolbar()
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initNearbyPlaceListAdapter()
    }

    private fun initToolbar() {
        AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.favoriteNearbyPlacesFragment)
        ).also {
            layout.toolbar.setupWithNavController(
                navController = findNavController(),
                configuration = it
            )
        }
    }

    private fun initNearbyPlaceListAdapter() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        layoutManager.isSmoothScrollbarEnabled = true
        layout.listPlace.layoutManager = layoutManager
        layout.listPlace.adapter = favoriteNearbyPlaceListAdapter

        lifecycleScope.launch {
            favoriteNearbyPlaceListAdapter.loadStateFlow.collectLatest {
                val isListEmpty =
                    it.refresh is LoadState.NotLoading && favoriteNearbyPlaceListAdapter.itemCount == 0
                if (isListEmpty) {
                    layout.layerEmptyHolder.visibility = View.VISIBLE
                } else {
                    layout.layerEmptyHolder.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.favoriteList.collectLatest {
                favoriteNearbyPlaceListAdapter.submitData(it)
            }
        }
    }

}