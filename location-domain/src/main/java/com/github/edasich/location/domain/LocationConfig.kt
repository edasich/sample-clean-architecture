package com.github.edasich.location.domain

import kotlin.time.DurationUnit
import kotlin.time.toDuration

class LocationConfig private constructor(
    val tag: String,
    val priority: LocationPriority,
    val interval: LocationInterval,
    val fastestInterval: LocationInterval,
    val distance: LocationDistance,
) {

    class Builder {

        private var tag: String = "default"
        private var priority: LocationPriority = LocationPriority.GPS_WIFI_MOBILE_CELL_PASSIVE
        private var interval: LocationInterval =
            LocationInterval(intervalInMillis = 1.toDuration(unit = DurationUnit.MINUTES).inWholeSeconds)
        private var fastestInterval: LocationInterval =
            LocationInterval(intervalInMillis = 30.toDuration(unit = DurationUnit.SECONDS).inWholeSeconds)
        private var distance: LocationDistance = LocationDistance(distanceInMeter = 100)

        fun setTag(tag: String): Builder {
            this.tag = tag
            return this@Builder
        }

        fun setPriority(priority: LocationPriority): Builder {
            this.tag = tag
            return this@Builder
        }

        fun setInterval(interval: LocationInterval): Builder {
            this.interval = interval
            return this@Builder
        }

        fun setFastestInterval(fastestInterval: LocationInterval): Builder {
            this.fastestInterval = fastestInterval
            return this@Builder
        }

        fun setDistance(distance: LocationDistance): Builder {
            this.distance = distance
            return this@Builder
        }

        fun build(): LocationConfig {
            return LocationConfig(
                tag = tag,
                priority = priority,
                interval = interval,
                fastestInterval = fastestInterval,
                distance = distance
            )
        }

    }

}

