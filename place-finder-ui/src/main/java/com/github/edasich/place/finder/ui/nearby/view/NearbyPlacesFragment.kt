package com.github.edasich.place.finder.ui.nearby.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.edasich.base.ui.EventObserver
import com.github.edasich.base.ui.bitmapFromDrawableRes
import com.github.edasich.place.finder.ui.R
import com.github.edasich.place.finder.ui.databinding.FragmentNearbyPlacesBinding
import com.github.edasich.place.finder.ui.nearby.model.PlaceMarkerView
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

@AndroidEntryPoint
class NearbyPlacesFragment : Fragment() {

    val viewModel: NearbyPlacesViewModel by viewModels()

    lateinit var layout: FragmentNearbyPlacesBinding

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
        viewModel.state.observe(viewLifecycleOwner) {
            render(screenUi = it)
        }
        viewModel.event.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is NearbyPlacesScreenEvent.ShowPlaceItem -> {
                    (layout.listPlace.layoutManager as LinearLayoutManager).scrollToPosition(it.placePosition)
                }
                is NearbyPlacesScreenEvent.ShowPlaceMarkerView -> {
                    layout.mapView.getMapboxMap().flyTo(
                        cameraOptions = cameraOptions {
                            center(Point.fromLngLat(it.markerLongitude, it.markerLatitude))
                            zoom(12.0)
                            pitch(50.0)
                        },
                        animationOptions = mapAnimationOptions {
                            duration(3000)
                        }
                    )
                }
            }
        })
    }

    private fun render(screenUi: NearbyPlacesScreenUi) {
        nearbyPlaceListAdapter.submitList(screenUi.placeList)
        clearPlaceMarkerViewListFromMap()
        addPlaceMarkerViewListToMap(placeMarkerViewList = screenUi.placeMarkerViewList)
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
    }

    private fun initNearbyPlaceListAdapter() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        layoutManager.isSmoothScrollbarEnabled = true
        layout.listPlace.layoutManager = layoutManager
        layout.listPlace.adapter = nearbyPlaceListAdapter
        layout.listPlace.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstCompletedVisiblePlace =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    viewModel.processRequest(
                        request = NearbyPlacesScreenRequest.OnPlaceItemViewed(
                            placePosition = firstCompletedVisiblePlace
                        )
                    )
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dy != 0) {
                    viewModel.processRequest(
                        request = NearbyPlacesScreenRequest.OnLoadMore(
                            totalLoadedItemCount = layoutManager.itemCount
                        )
                    )
                }
            }
        })
    }

    private fun clearPlaceMarkerViewListFromMap() {
        pointAnnotationManager.deleteAll()
    }

    private fun addPlaceMarkerViewListToMap(
        placeMarkerViewList: List<PlaceMarkerView>
    ) {
        requireContext().bitmapFromDrawableRes(
            context = requireContext(),
            resourceId = R.drawable.ic_place_marker
        )?.let { markerIcon ->
            placeMarkerViewList
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

}