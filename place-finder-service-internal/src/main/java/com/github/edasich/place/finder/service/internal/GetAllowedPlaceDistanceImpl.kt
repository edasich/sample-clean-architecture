package com.github.edasich.place.finder.service.internal

import com.github.edasich.palce.finder.service.GetAllowedPlaceDistance
import com.github.edasich.place.finder.domain.AllowedDistance
import javax.inject.Inject

class GetAllowedPlaceDistanceImpl @Inject constructor() : GetAllowedPlaceDistance {

    override fun invoke(): AllowedDistance = AllowedDistance(distanceInMeter = 500)

}