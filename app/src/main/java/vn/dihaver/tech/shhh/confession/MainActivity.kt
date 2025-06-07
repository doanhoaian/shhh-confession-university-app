package vn.dihaver.tech.shhh.confession

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import vn.dihaver.tech.shhh.confession.core.data.local.datastore.SessionManager
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.navigation.AppNavGraph
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    private var isReadyToShowUI = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !isReadyToShowUI }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            SystemBarStyle.dark(Color.TRANSPARENT),
            SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            ShhhTheme {
                val navController = rememberNavController()
                var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }

                LaunchedEffect(Unit) {
                    isLoggedIn = sessionManager.isLoggedIn()
                    Log.i("AAA-MainActivity", sessionManager.userSession.first().toString())
                    isReadyToShowUI = true
                }

                if (isLoggedIn != null) {
                    AppNavGraph(
                        navController = navController,
                        isLoggedIn = isLoggedIn!!
                    )
                }
            }
        }
    }
}