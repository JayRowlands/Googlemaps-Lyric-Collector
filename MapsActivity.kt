package com.example.geolyrical

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlin.random.Random
import com.google.android.gms.maps.model.Marker
import android.widget.*
import java.lang.IndexOutOfBoundsException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    val PERMISSION_ID = 42
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var markers: MutableList<Marker> = ArrayList()
    private lateinit var lastLoc: Location

    private var classicLines: MutableList<String> = ArrayList()
    private var currentLines: MutableList<String> = ArrayList()

    var classicLyricsCollected = Array<String?>(9) { null }
    var currentLyricsCollected = Array<String?>(9) { null }
    var numLyrics : Int = 0


    var classicMode: Boolean = Mode.mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val guessButton: ImageView = this.findViewById(R.id.guessButton) as ImageView
        guessButton.setOnClickListener {
            if (classicMode == true) {
                if(this.numLyrics == 0) {
                    Toast.makeText(this, "You must collect a lyric", Toast.LENGTH_SHORT).show()
                } else {
                    for (i in 0 until classicLyricsCollected.size) {
                        Lyrics.lyricsCollected[i] = classicLyricsCollected[i]
                    }
                    startActivity(Intent(this, GuessActivity::class.java))
                }

            } else if (classicMode == false) {
                if (this.numLyrics == 0) {
                    Toast.makeText(this, "You must collect a lyric", Toast.LENGTH_SHORT).show()
                } else {
                    for (i in 0 until currentLyricsCollected.size) {
                        Lyrics.lyricsCollected[i] = currentLyricsCollected[i]
                    }
                    startActivity(Intent(this, GuessActivity::class.java))
                }
            }
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        requestNewLocationData()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.i("myLocation", "Map Ready")
        mMap = googleMap

        mMap.mapType = MAP_TYPE_SATELLITE
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.isMyLocationEnabled = true

        val lastLoc = LatLng(51.619543, -3.878634)
        val zoomLevel = 18f
        selectSong()

        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                selectMarker(marker)
                return false
            }
        })
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, zoomLevel))
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        var lat = location.latitude
                        var long = location.longitude

                        var accuracy = location.accuracy

                        val lastLoc = LatLng(lat, long)
                        val zoomLevel = 18f
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, zoomLevel))
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }

    }

    private fun requestNewLocationData() {
        Log.i("myLocation", "request")
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 2000
        mLocationRequest.fastestInterval = 1000

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.i("myLocation", "Callback")
            var mLastLocation: Location = locationResult.lastLocation
            lastLoc = mLastLocation
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun selectSong() {
        if (classicMode == true) {
            val classicSongNum = Random.nextInt(0, 18)
            placeMarkers(classicSongNum)
        } else if (classicMode == false) {
            val currentSongNum = Random.nextInt(0, 19)
            placeMarkers(currentSongNum)
        }

    }

    private fun placeMarkers(songNum : Int) {

        if (classicMode == true) {
            readClassical(songNum)

            for (j in 0 until classicLines.size - 1) {
                var long = Random.nextDouble(-3.882634, -3.873224)
                var lat = Random.nextDouble(51.616543, 51.620104)

                var marker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(lat, long))
                        .title("Lyric")
                )
                marker.tag = classicLines[j]
                markers.add(marker)
            }
        } else if (classicMode == false) {
            readCurrent(songNum)

            for (j in 0 until currentLines.size - 1) {
                var long = Random.nextDouble(-3.882634, -3.873224)
                var lat = Random.nextDouble(51.616543, 51.620104)

                var marker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(lat, long))
                        .title("Lyric")
                )
                marker.tag = currentLines[j]
                markers.add(marker)
                }
        }
    }
    fun selectMarker(marker: Marker) {
        val discoverDist = 5000f
        val markerLocation = Location("")
        markerLocation.latitude = marker.position.latitude
        markerLocation.longitude = marker.position.longitude

        var distanceTo: Float = markerLocation.distanceTo(lastLoc)

        if (distanceTo <= discoverDist) {
            if (classicMode == true) {
                addClassicCollected(marker)
                this.numLyrics++
            } else if (classicMode == false) {
                addCurrentCollected(marker)
                this.numLyrics++
            }
            marker.remove()
            Toast.makeText(this, "Lyric collected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Not in range", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addClassicCollected(marker: Marker) {
        for (i in 0 until classicLines.size-1) {
            if (marker.tag.toString() == classicLines[i]) {
                classicLyricsCollected[i] = marker.tag.toString()
            }
        }
    }
    private fun addCurrentCollected(marker: Marker) {
        for (i in 0 until currentLines.size -1) {
            if (marker.tag.toString() == currentLines[i]) {
                currentLyricsCollected[i] = marker.tag.toString()
            }
        }
    }


    fun readClassical(i: Int) {
        var classicList: Array<String> = assets.list("Classic")!!
        var bufferReader = assets.open("Classic/" + classicList[i]).bufferedReader()
        var data = bufferReader.use {
            it.readText()
        }
        var minRange = (0 until data.lines().size - 8).random()
        var maxRange = minRange + 8

        for (a in minRange..maxRange) {
                classicLines.add(data.lines()[a])
        }

        var stringFull = classicList[i].split("(", ")")
        var artistName = stringFull.get(0).replace("_", " ")
        var songName = stringFull.get(1).replace("_", " ")

        Lyrics.artistOfSong = artistName
        Lyrics.songName = songName
        //  Log.d("readFromAsset", data)

    }

    fun readCurrent(i: Int) {
        var currentList: Array<String> = assets.list("Current")!!
        var bufferReader = assets.open("Current/" + currentList[i]).bufferedReader()
        var data = bufferReader.use {
            it.readText()
        }

        var minRange = (0 until data.lines().size - 8).random()
        var maxRange = minRange + 8

        for (a in minRange..maxRange) {
            currentLines.add(data.lines()[a])
        }

        var stringFull = currentList[i].split("(", ")")
        var artistName = stringFull.get(0).replace("_", " ")
        var songName = stringFull.get(1).replace("_", " ")

        Lyrics.artistOfSong = artistName
        Lyrics.songName = songName
        // Log.d("readFromAsset", data)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (classicMode == true) {
            getMenuInflater().inflate(R.menu.menu_classic, menu)
        } else if (classicMode == false) {
            getMenuInflater().inflate(R.menu.menu_current, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }
            R.id.collection -> {
                startActivity(Intent(this, CollectionActivity::class.java))
                return true
            }
            R.id.friends -> {
                startActivity(Intent(this, FriendActivity::class.java))
                return true
            }
            R.id.mainMenu -> {
                startActivity(Intent(this, MainActivity::class.java))
                return true
            }
            R.id.mode -> {
                if (item.title == "Classic") {
                    Mode.mode = false
                    startActivity(Intent(this, MapsActivity::class.java))
                } else if (item.title == "Current") {
                    Mode.mode = true
                    startActivity(Intent(this, MapsActivity::class.java))
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
