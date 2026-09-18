package com.volumetile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.media.AudioManager
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

/**
 * Single-purpose Quick Settings tile service that triggers Android's native system
 * volume panel overlay when tapped.
 *
 * Requirements satisfied:
 * - Single TileService declared with android.permission.BIND_QUICK_SETTINGS_TILE
 *   and intent-filter android.service.quicksettings.action.QS_TILE.
 * - Triggers Android's native volume panel overlay via adjustSuggestedStreamVolume
 *   using USE_DEFAULT_STREAM_TYPE so it context-switches dynamically (media/call/ring/alarm).
 * - Zero third-party dependencies, zero AndroidX.
 * - Zero permissions beyond what TileService itself requires.
 * - Queries stream volume and mute state in onStartListening() to update tile state
 *   and subtitle with volume %.
 * - Registers BroadcastReceiver for VOLUME_CHANGED_ACTION only while listening
 *   (between onStartListening() and onStopListening()), with zero background footprint
 *   when the QS panel is closed.
 * - Fully offline, no analytics, ads, or telemetry.
 *
 * NOTE FOR COLOROS / REALME UI / AGGRESSIVE OEM BATTERY SAVERS:
 * On ColorOS/Realme UI, the user should manually exclude the app from battery optimization
 * (Settings -> Battery -> App battery management -> Allow background activity / Don't optimize)
 * for reliable tile binding — OEM background management may prevent TileService binding otherwise,
 * and this cannot be set programmatically due to Android platform security restrictions.
 */
class VolumeTileService : TileService() {

    private var isReceiverRegistered = false

    private val volumeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateTileState()
        }
    }

    override fun onClick() {
        super.onClick()
        try {
            val am = applicationContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
            am.adjustSuggestedStreamVolume(
                AudioManager.ADJUST_SAME,
                AudioManager.USE_DEFAULT_STREAM_TYPE,
                AudioManager.FLAG_SHOW_UI
            )
        } catch (_: Exception) {
            // Failsafe catch to ensure tile interaction never crashes on modified OEM ROMs
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
        if (!isReceiverRegistered) {
            try {
                val filter = IntentFilter(VOLUME_CHANGED_ACTION).apply {
                    addAction(AudioManager.RINGER_MODE_CHANGED_ACTION)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    registerReceiver(volumeReceiver, filter, Context.RECEIVER_EXPORTED)
                } else {
                    registerReceiver(volumeReceiver, filter)
                }
                isReceiverRegistered = true
            } catch (_: Exception) {
                // Failsafe catch for broadcast receiver registration
            }
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        unregisterVolumeReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterVolumeReceiver()
    }

    private fun unregisterVolumeReceiver() {
        if (isReceiverRegistered) {
            try {
                unregisterReceiver(volumeReceiver)
            } catch (_: IllegalArgumentException) {
                // Receiver was already unregistered
            } catch (_: Exception) {
                // Failsafe catch
            }
            isReceiverRegistered = false
        }
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        try {
            val am = applicationContext.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return

            // Context-aware stream detection: voice call if in a call/communication, otherwise media stream
            val streamType = if (am.mode == AudioManager.MODE_IN_CALL || am.mode == AudioManager.MODE_IN_COMMUNICATION) {
                AudioManager.STREAM_VOICE_CALL
            } else {
                AudioManager.STREAM_MUSIC
            }

            val currentVolume = am.getStreamVolume(streamType)
            val maxVolume = am.getStreamMaxVolume(streamType)
            val isMuted = am.isStreamMute(streamType) || currentVolume == 0

            val volumePercent = if (maxVolume > 0) {
                (currentVolume * 100) / maxVolume
            } else {
                0
            }

            tile.state = if (isMuted) Tile.STATE_INACTIVE else Tile.STATE_ACTIVE
            tile.icon = Icon.createWithResource(
                this,
                if (isMuted) R.drawable.ic_volume_off else R.drawable.ic_volume_up
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                tile.subtitle = if (isMuted) {
                    getString(R.string.volume_muted)
                } else {
                    "$volumePercent%"
                }
            }

            tile.updateTile()
        } catch (_: Exception) {
            // Failsafe catch: ensures tile updates never crash SystemUI
        }
    }

    companion object {
        /**
         * System broadcast action sent when audio volume changes.
         * Defined directly as a string constant because android.media.VOLUME_CHANGED_ACTION
         * is an internal (@hide) framework constant not exposed in the public SDK stubs.
         */
        private const val VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION"
    }
}
