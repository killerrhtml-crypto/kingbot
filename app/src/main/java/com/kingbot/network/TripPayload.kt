package com.kingbot.network

data class TripPayload(
    val tripId: String,
    val price: Double,
    val pickupDistanceKm: Double,
    val totalDistanceKm: Double,
    val destinationName: String,
    val rawJson: String
)
