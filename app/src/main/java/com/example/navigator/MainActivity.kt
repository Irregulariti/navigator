package com.example.navigator

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.navigator.ui.theme.NavigatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            if (!wifi.isWifiEnabled())
                if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                    wifi.setWifiEnabled(true);
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
}

public class WiFiAP(
    SSID: String,
    BSSID: String,
    Channel: Int,
    Level: Int,
    Security: Int,
    WPS: Boolean
) {
    private var SSID: String
    private var BSSID: String
    private var Channel: Int
    private var Level: Int
    private var Security: Int;// 0 — открытая сеть; 1 — WEP; 2 — WPA/WPA2
    private var WPS: Boolean  // WPS?

    init {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.Channel = Channel;
        this.Level = Level;
        this.Security = Security;
        this.WPS = WPS;
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