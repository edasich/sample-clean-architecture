package com.github.edasich.palce.finder.service

interface StopSearchingNearbyPlace {
    suspend operator fun invoke()
}