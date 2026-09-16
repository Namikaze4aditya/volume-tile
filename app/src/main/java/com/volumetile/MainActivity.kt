package com.volumetile

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.Button

/**
 * Trivial one-time onboarding activity explaining how to add the Quick Settings tile.
 * Uses core android.app.Activity without AndroidX or any third-party dependencies.
 *
 * NOTE FOR COLOROS / REALME UI / AGGRESSIVE OEM BATTERY SAVERS:
 * On ColorOS/Realme UI, users should manually exclude the app from battery optimization
 * (Settings -> Battery -> App battery management -> Allow background activity / Don't optimize)
 * for reliable tile binding — cannot be set programmatically.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnTestVolume).setOnClickListener {
            val am = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.adjustSuggestedStreamVolume(
                AudioManager.ADJUST_SAME,
                AudioManager.USE_DEFAULT_STREAM_TYPE,
                AudioManager.FLAG_SHOW_UI
            )
        }

        findViewById<Button>(R.id.btnDone).setOnClickListener {
            finish()
        }
    }
}
