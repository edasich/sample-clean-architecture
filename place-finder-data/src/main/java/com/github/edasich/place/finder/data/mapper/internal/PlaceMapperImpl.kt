package com.github.edasich.place.finder.data.mapper.internal

import android.graphics.PointF
import com.github.edasich.location.domain.DeviceLocation
import com.github.edasich.place.finder.data.local.model.PlaceEntity
import com.github.edasich.place.finder.data.local.model.PlaceEntityWithoutFavored
import com.github.edasich.place.finder.data.local.model.PlaceToSearchParams
import com.github.edasich.place.finder.data.mapper.PlaceMapper
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiRequest
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiResponse
import com.github.edasich.place.finder.data.remote.rest.model.PlaceApiResponse
import com.github.edasich.place.finder.domain.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class PlaceMapperImpl @Inject constructor(
    private val coroutineContext: CoroutineContext
) : PlaceMapper {

    override suspend fun mapToPlaces(
        entities: List<PlaceEntity>
    ): List<Place> {
        return withContext(context = coroutineContext) {
            entities.map {
                mapToPlaceOrNull(entity = it)!!
            }
        }
    }

    override suspend fun mapToPlace(
        placeApiResponse: PlaceApiResponse
    ): Place {
        return placeApiResponse.let {
            Place(
                id = PlaceId(id = it.id),
                name = PlaceName(name = it.name),
                address = PlaceAddress(
                    geocode = PlaceGeocode(
                        latitude = PlaceLatitude(latitude = it.geocode.main.latitude),
                        longitude = PlaceLongitude(longitude = it.geocode.main.longitude)
                    ),
                    detail = PlaceAddressDetail(
                        address = it.location.formattedAddress
                    )
                ),
                placeFavoriteStatus = PlaceFavoriteStatus.NotFavored
            )
        }
    }

    override suspend fun mapToPlace(entity: PlaceEntity): Place {
        return entity.let {
            Place(
                id = PlaceId(id = it.id),
                name = PlaceName(name = it.name),
                address = PlaceAddress(
                    geocode = PlaceGeocode(
                        latitude = PlaceLatitude(latitude = it.latitude),
                        longitude = PlaceLongitude(longitude = it.longitude)
                    ),
                    detail = PlaceAddressDetail(
                        address = it.address
                    )
                ),
                placeFavoriteStatus = if (it.isFavored) {
                    PlaceFavoriteStatus.Favored
                } else {
                    PlaceFavoriteStatus.NotFavored
                }
            )
        }
    }

    override suspend fun mapToPlaceOrNull(entity: PlaceEntity?): Place? {
        return entity?.let {
            mapToPlace(entity = it)
        }
    }

    override suspend fun mapToPlaceEntity(
        place: Place
    ): PlaceEntity {
        return place.let {
            PlaceEntity(
                id = place.id.id,
                name = it.name.name,
                latitude = place.address.geocode.latitude.latitude,
                longitude = place.address.geocode.longitude.longitude,
                address = it.address.detail.address,
                isFavored = when (it.placeFavoriteStatus) {
                    PlaceFavoriteStatus.Favored -> true
                    PlaceFavoriteStatus.NotFavored -> false
                }
            )
        }
    }

    override suspend fun mapToPlaceEntities(
        apiResponse: NearbyPlacesApiResponse
    ): List<PlaceEntityWithoutFavored> {
        return withContext(context = coroutineContext) {
            apiResponse.places.map {
                PlaceEntityWithoutFavored(
                    id = it.id,
                    name = it.name,
                    latitude = it.geocode.main.latitude,
                    longitude = it.geocode.main.longitude,
                    address = it.location.formattedAddress
                )
            }
        }
    }

    override fun mapToPlaceToSearchParams(
        allowedDistance: AllowedDistance,
        deviceLocation: DeviceLocation
    ): PlaceToSearchParams {
        val pointCenter = PointF(
            deviceLocation.latitude.latitude.toFloat(),
            deviceLocation.longitude.longitude.toFloat()
        )
        val distance = allowedDistance.distanceInMeter
        val pointNorth = calculateDerivedPosition(pointCenter, distance * 1.1, 0.0)
        val pointEast = calculateDerivedPosition(pointCenter, distance * 1.1, 90.0)
        val pointSouth = calculateDerivedPosition(pointCenter, distance * 1.1, 180.0)
        val pointWest = calculateDerivedPosition(pointCenter, distance * 1.1, 270.0)

        return PlaceToSearchParams(
            pointSouthX = pointSouth.x,
            pointNorthX = pointNorth.x,
            pointEastY = pointEast.y,
            pointWestY = pointWest.y,
        )
    }

    override suspend fun mapToNearbyPlacesApiRequest(
        allowedDistance: AllowedDistance,
        currentDeviceLocation: DeviceLocation
    ): NearbyPlacesApiRequest {
        return withContext(context = coroutineContext) {
            NearbyPlacesApiRequest(
                latLng = "${currentDeviceLocation.latitude.latitude},${currentDeviceLocation.longitude.longitude}",
                radius = allowedDistance.distanceInMeter
            )
        }
    }

    /**
     * Calculates the end-point from a given source at a given range (meters)
     * and bearing (degrees). This methods uses simple geometry equations to
     * calculate the end-point.
     *
     * @param point
     * Point of origin
     * @param range
     * Range in meters
     * @param bearing
     * Bearing in degrees
     * @return End-point from the source given the desired range and bearing.
     */
    @Suppress("SpellCheckingInspection")
    private fun calculateDerivedPosition(
        point: PointF,
        range: Double,
        bearing: Double
    ): PointF {
        val earthRadius = 6371000.0 // m
        val latA = Math.toRadians(point.x.toDouble())
        val lonA = Math.toRadians(point.y.toDouble())
        val angularDistance = range / earthRadius
        val trueCourse = Math.toRadians(bearing)
        var lat = asin(
            sin(latA) * cos(angularDistance) +
                    (cos(latA) * sin(angularDistance)
                            * cos(trueCourse))
        )
        val dlon = atan2(
            sin(trueCourse) * sin(angularDistance) * cos(latA),
            cos(angularDistance) - sin(latA) * sin(lat)
        )
        var lon =
            (lonA + dlon + Math.PI) % (Math.PI * 2) - Math.PI
        lat = Math.toDegrees(lat)
        lon = Math.toDegrees(lon)
        return PointF(lat.toFloat(), lon.toFloat())
    }

}