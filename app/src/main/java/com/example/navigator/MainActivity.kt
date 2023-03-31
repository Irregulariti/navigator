package com.example.navigator

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.navigator.ui.theme.NavigatorTheme


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE),
                    1
                )
                makeEnableLocationServices(activity.applicationContext)
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CHANGE_WIFI_STATE),
                    1
                )
            }

            /* включает экран включения службы по определению местоположения */
            fun makeEnableLocationServices(context: Context) {
                // TODO: перед вызовом этой функции надо рассказать пользователю, зачем Вам доступ к местоположению
                val lm: LocationManager =
                    context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val gpsEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                val networkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!gpsEnabled && !networkEnabled) {
                    context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
            NavigatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun detectWifi() {
        println("gffg")
        this.wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
        this.wifiManager!!.startScan()
        this.wifiList = this.wifiManager!!.scanResults
        Log.d("TAG", wifiList.toString())
        this.nets = mutableListOf()
        for (i in 0 until wifiList!!.size) {
            println("cikl")
            val item: String = wifiList!!.get(i).toString()
            val vector_item = item.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val item_essid = vector_item[0]
            val item_capabilities = vector_item[2]
            val item_level = vector_item[3]
            val ssid =
                item_essid.split(": ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
            val security =
                item_capabilities.split(": ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
            val level =
                item_level.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            nets.add(Element(ssid, security, level))
        }
        println("GGF")
        println(nets)
        for (i in nets) {
            println(i)
        }
    }
}

public class Element(
    title: String,
    security: String,
    level: String
) {
    private var title: String
    private var security: String
    private var level: String

    init {
        this.title = title;
        this.security = security;
        this.level = level
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NavigatorTheme {
        Greeting("Android")
    }
}

