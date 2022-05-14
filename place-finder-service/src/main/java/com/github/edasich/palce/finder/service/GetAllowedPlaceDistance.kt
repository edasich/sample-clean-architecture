package com.github.edasich.palce.finder.service

import com.github.edasich.place.finder.domain.AllowedDistance

interface GetAllowedPlaceDistance {
    operator fun invoke(): AllowedDistance
}