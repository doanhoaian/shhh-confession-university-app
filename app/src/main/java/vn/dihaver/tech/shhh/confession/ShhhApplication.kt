package vn.dihaver.tech.shhh.confession

import android.app.Application
import coil.Coil
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import vn.dihaver.tech.shhh.confession.core.util.createCustomImageLoader

@HiltAndroidApp
class ShhhApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        Coil.setImageLoader(createCustomImageLoader(this))
    }
}