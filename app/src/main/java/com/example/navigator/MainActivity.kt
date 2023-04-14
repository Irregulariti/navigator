package com.example.navigator

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.navigator.ui.theme.NavigatorTheme
import java.io.FileOutputStream
import java.lang.Math.abs


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val message = remember { mutableStateOf("") }
            TextField(value = message.value,
                onValueChange = { newText -> message.value = newText }, Modifier.padding(30.dp))

            Button(onClick = {
                val wifiManager: WifiManager = getSystemService(WIFI_SERVICE) as WifiManager

                fun scanSuccess() {
                    val results = if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        wifiManager.scanResults
                    } else {
                        // Permission is not granted
                        listOf<ScanResult>()
                    }

                    var text = "${message.value}: \n"
                    for (i in results) {
                        text += i.SSID + " - "
                        text += kotlin.math.abs(i.level).toString() + "\n"
                    }
                    text += "\n".repeat(3)
                    val fos: FileOutputStream = openFileOutput("results", MODE_APPEND)
                    fos.write(text.toByteArray())
                    fos.close()
                }

                fun scanFailure() {
                    // handle failure: new scan did NOT succeed
                    // consider using old scan results: these are the OLD results!
                    val results = wifiManager.scanResults
                    println(results)
                }

                val success = wifiManager.startScan()
//                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(myIntent)
                println(success)
                if (!success) {
                    scanFailure()
                }

                val wifiScanReceiver = object : BroadcastReceiver() {

                    override fun onReceive(context: Context, intent: Intent) {
                        val success =
                            intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                        if (success) {
                            scanSuccess()
                        } else {
                            scanFailure()
                        }
                    }
                }

                val intentFilter = IntentFilter()
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                registerReceiver(wifiScanReceiver, intentFilter)
            }
            ) {
                Text("Click")
            }
        }
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

