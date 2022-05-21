package com.github.edasich.place.finder.ui.nearby.view

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.edasich.base.ui.bitmapFromDrawableRes
import com.github.edasich.base.ui.showProgress
import com.github.edasich.place.finder.domain.SearchNearbyPlaceStatus
import com.github.edasich.place.finder.ui.R
import com.github.edasich.place.finder.ui.databinding.FragmentNearbyPlacesBinding
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NearbyPlacesFragment : Fragment() {

    val viewModel: NearbyPlacesViewModel by viewModels()

    lateinit var layout: FragmentNearbyPlacesBinding

    private val locationPermissionForResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val areLocationPermissionsGranted = it.all { permission ->
                permission.value
            }
            if (areLocationPermissionsGranted) {
                viewModel.processRequest(
                    request = NearbyPlacesScreenRequest.OnLocationPermissionsGranted
                )
            } else {
                viewModel.processRequest(
                    request = NearbyPlacesScreenRequest.OnLocationPermissionsDenied
                )
            }
        }

    private val locationResolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    viewModel.processRequest(
                        request = NearbyPlacesScreenRequest.OnLocationAcceptedToBeEnabled
                    )
                }
                Activity.RESULT_CANCELED -> {
                    viewModel.processRequest(
                        request = NearbyPlacesScreenRequest.OnLocationDeniedToBeEnabled
                    )
                }
            }
        }

    private val nearbyPlaceListAdapter: NearbyPlaceListAdapter by lazy {
        NearbyPlaceListAdapter {
            viewModel.processRequest(
                request = NearbyPlacesScreenRequest.OnFavoritePlaceClicked(
                    place = it
                )
            )
        }
    }

    lateinit var pointAnnotationManager: PointAnnotationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentNearbyPlacesBinding.inflate(inflater, container, false)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMap()
        initNearbyPlaceListAdapter()
        initSearchPlace()
        handleEvents()
    }

    private fun initMap() {
        layout.mapView.getMapboxMap().loadStyleUri(Style.OUTDOORS)
        val annotationApi = layout.mapView.annotations
        pointAnnotationManager =
            annotationApi.createPointAnnotationManager()
        pointAnnotationManager.addClickListener(u = OnPointAnnotationClickListener {
            viewModel.processRequest(
                request = NearbyPlacesScreenRequest.OnPlaceMarkerClicked(
                    markerLatitude = it.point.latitude(),
                    markerLongitude = it.point.longitude()
                )
            )
            true
        })

        lifecycleScope.launch {
            viewModel.markerListFlow.collectLatest {
                clearPlaceMarkerViewListFromMap()
                addPlaceMarkerViewListToMap(nearbyPlaceItemList = it)
            }
        }
    }

    private fun initNearbyPlaceListAdapter() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        layoutManager.isSmoothScrollbarEnabled = true
        layout.listPlace.layoutManager = layoutManager
        layout.listPlace.adapter = nearbyPlaceListAdapter
        layout.listPlace.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    viewModel.processRequest(
                        request = NearbyPlacesScreenRequest.OnPlaceItemScrolled(
                            placePosition = firstVisibleItemPosition,
                        )
                    )
                }
            }

        })

        lifecycleScope.launch {
            viewModel.placeListFlow.collectLatest {
                nearbyPlaceListAdapter.submitData(it)
            }
        }
    }

    private fun initSearchPlace() {
        layout.btnPlaceSearch.setOnClickListener {
            viewModel.processRequest(
                request = NearbyPlacesScreenRequest.OnSearchPlaceClicked
            )
        }

        lifecycleScope.launch {
            viewModel.searchingPlaceFlow.collectLatest {
                when (it) {
                    SearchNearbyPlaceStatus.STARTED -> {
                        layout.btnPlaceSearch.showProgress(
                            showProgress = true,
                            iconSource = R.drawable.ic_place_search
                        )
                    }
                    SearchNearbyPlaceStatus.STOPPED -> {
                        layout.btnPlaceSearch.showProgress(
                            showProgress = false,
                            iconSource = R.drawable.ic_place_search
                        )
                    }
                }
            }
        }
    }

    private fun handleEvents() {
        viewModel.events.onEach {
            when (it) {
                NearbyPlacesScreenEvent.AskGrantLocationPermissions -> {
                    promptLocationPermissions()
                }
                is NearbyPlacesScreenEvent.AskEnableLocation -> {
                    promptEnableLocation(pendingIntent = it.pendingIntent)
                }
                is NearbyPlacesScreenEvent.ShowPlaceItem -> {
                    showPlaceItemInList(placePosition = it.placePosition)
                }
                is NearbyPlacesScreenEvent.ShowPlaceMarker -> {
                    showPlaceMarkerInMap(
                        latitude = it.markerLatitude,
                        longitude = it.markerLongitude
                    )
                }
            }
        }.launchIn(scope = lifecycleScope)
    }

    private fun clearPlaceMarkerViewListFromMap() {
        pointAnnotationManager.deleteAll()
    }

    private fun addPlaceMarkerViewListToMap(
        nearbyPlaceItemList: List<NearbyPlaceItem>
    ) {
        requireContext().bitmapFromDrawableRes(
            context = requireContext(),
            resourceId = R.drawable.ic_place_marker
        )?.let { markerIcon ->
            nearbyPlaceItemList
                .map {
                    PointAnnotationOptions()
                        .withPoint(point = Point.fromLngLat(it.longitude, it.latitude))
                        .withIconImage(iconImageBitmap = markerIcon)
                }
                .also {
                    pointAnnotationManager.create(options = it)
                }
        }
    }

    private fun promptLocationPermissions() {
        locationPermissionForResult.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun promptEnableLocation(pendingIntent: PendingIntent) {
        val locationIntentSenderRequest =
            IntentSenderRequest.Builder(pendingIntent).build()
        locationResolutionForResult.launch(locationIntentSenderRequest)
    }

    private fun showPlaceItemInList(placePosition: Int) {
        (layout.listPlace.layoutManager as LinearLayoutManager).scrollToPosition(placePosition)
    }

    private fun showPlaceMarkerInMap(
        latitude: Double,
        longitude: Double
    ) {
        layout.mapView.getMapboxMap().flyTo(
            cameraOptions = cameraOptions {
                center(Point.fromLngLat(longitude, latitude))
                zoom(15.0)
                pitch(50.0)
            },
            animationOptions = mapAnimationOptions {
                duration(3000)
            }
        )
    }

}