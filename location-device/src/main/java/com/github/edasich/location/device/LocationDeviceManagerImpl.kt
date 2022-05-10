package com.github.edasich.location.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.github.edasich.location.device.mapper.LocationDeviceMapper
import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.location.domain.LocationConfig
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocationDeviceManagerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationDeviceMapper: LocationDeviceMapper
) : LocationDeviceManager {

    private val currentLocationConfig = AtomicReference<LocationConfig>()

    private val locationEventEmitter = MutableSharedFlow<DeviceLocation>(
        replay = 1
    )
    private val locationEventReceiver = locationEventEmitter.asSharedFlow()

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(result: LocationResult) {
            locationDeviceMapper
                .mapToDeviceLocation(
                    locationResult = result,
                    locationConfig = currentLocationConfig.get()
                )
                .also {
                    locationEventEmitter.tryEmit(value = it)
                }
        }

    }

    @SuppressLint("MissingPermission")
    override fun startCollectingLocation(locationConfig: LocationConfig) {
        if (isAccessLocationPermissionGranted()) {
            currentLocationConfig.set(locationConfig)
            val locationRequest = createLocationRequest(locationConfig = locationConfig)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun stopCollectingLocation() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun getLocationCollector(): Flow<DeviceLocation> {
        return locationEventReceiver
    }

    override fun isAccessLocationPermissionGranted(): Boolean {
        val accessFineLocationPermissionStatus =
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val accessCoarseLocationPermissionStatus =
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return accessFineLocationPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                accessCoarseLocationPermissionStatus == PackageManager.PERMISSION_GRANTED
    }

    private fun createLocationRequest(locationConfig: LocationConfig): LocationRequest {
        return LocationRequest
            .create()
            .apply {
                val locationPriority: Int = locationDeviceMapper.mapToLocationPriorityRequest(
                    locationPriority = locationConfig.priority
                )
                priority = locationPriority
                interval = locationConfig.interval.intervalInMillis
                fastestInterval = locationConfig.fastestInterval.intervalInMillis
                smallestDisplacement = locationConfig.distance.distanceInMeter.toFloat()
                isWaitForAccurateLocation = false
            }
    }

}