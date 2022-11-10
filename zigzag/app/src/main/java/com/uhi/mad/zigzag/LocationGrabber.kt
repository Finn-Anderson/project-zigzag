package com.uhi.mad.zigzag

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*

class LocationGrabber {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private fun countryFallback(ctx: Context): String {
        return if (Build.VERSION.SDK_INT >= 24) {
            ctx.resources.configuration.locales[0].displayCountry
        } else {
            ctx.resources.configuration.locale.displayCountry
        }
    }

    fun getLocation(ctx: Context, act: Activity, callback: (country: String) -> Unit) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(act.applicationContext)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        locationCallback = object: LocationCallback() {
            private var country: String = ""

            fun returnCountry() {
                if (country.isEmpty()) {
                    country = countryFallback(ctx)
                }
                callback.invoke(country)
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                if (!p0.isLocationAvailable) {
                    returnCountry()
                }
            }

            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val geocoder = Geocoder(ctx, Locale.getDefault())

                    if (Build.VERSION.SDK_INT >= 33) {
                        val geocodeListener = Geocoder.GeocodeListener { addresses ->
                            country = addresses[0].countryName
                            Handler(ctx.mainLooper).post { returnCountry() }
                        }
                        geocoder.getFromLocation(location.latitude, location.longitude, 1, geocodeListener)
                    } else {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (addresses != null) {
                            country = addresses[0].countryName
                        }
                        returnCountry()
                    }

                    break
                }

                if (locationResult.locations.isEmpty()) {
                    returnCountry()
                }

                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            callback.invoke(countryFallback(ctx))
        }

    }
}