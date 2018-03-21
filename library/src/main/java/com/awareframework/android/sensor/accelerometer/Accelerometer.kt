package com.awareframework.android.sensor.accelerometer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.Log
import com.awareframework.android.core.model.ISensorController
import com.awareframework.android.core.db.Engine
import com.awareframework.android.core.model.SensorConfig
import com.awareframework.android.core.model.SensorObserver
import com.awareframework.android.sensor.accelerometer.AccelerometerSensor.Companion.startService
import com.awareframework.android.sensor.accelerometer.AccelerometerSensor.Companion.stopService


/**
 * Main interaction class with the sensor
 *
 * @author  sercant
 * @date 19/02/2018
 */
class Accelerometer private constructor(
        private var context: Context,
        config: AccelerometerConfig = AccelerometerConfig()
) : ISensorController {

    companion object {
        const val ACTION_AWARE_ACCELEROMETER_START = "com.aware.android.sensor.accelerometer.SENSOR_START"
        const val ACTION_AWARE_ACCELEROMETER_STOP = "com.aware.android.sensor.accelerometer.SENSOR_STOP"
        const val ACTION_AWARE_ACCELEROMETER_LABEL = "com.aware.android.sensor.accelerometer.SET_LABEL"

        val defaultConfig: AccelerometerConfig = AccelerometerConfig()
    }

    internal val TAG = "com.aware.accelerometer"

    var config: AccelerometerConfig = config
        private set

    data class AccelerometerConfig(
            /**
             * Accelerometer interval in hertz per second: e.g.,
             * 0 - fastest
             * 1 - sample per second
             * 5 - sample per second
             * 20 - sample per second
             */
            var interval: Int = 5,

            /**
             * Period to save data in minutes. (optional)
             */
            var period: Float = 1f,

            /**
             * Accelerometer threshold (float).  Do not record consecutive points if
             * change in value of all axes is less than this.
             */
            var threshold: Float = 0f,

            var sensorObserver: SensorObserver? = null,

            var wakeLockEnabled: Boolean = true
    ) : SensorConfig(dbPath = "aware_accelerometer")

    class Builder(context: Context) : SensorConfig.Builder<AccelerometerConfig>(context) {

        private val config: AccelerometerConfig = AccelerometerConfig()

        /**
         * @param interval sample count per second in hertz. (*fastest• default = 0)
         */
        fun setInterval(interval: Int) = apply { config.interval = interval }
        fun setThreshold(threshold: Float) = apply { config.threshold = threshold }

        /**
         * @param period period of database saves in minutes. (default = 1.0)
         */
        fun setPeriod(period: Float) = apply { config.period = period }

        /**
         * @param sensorObserver callback for live data updates.
         */
        fun setSensorObserver(sensorObserver: SensorObserver) = apply { config.sensorObserver = sensorObserver }

//        /**
//         * @param deviceId id of the device that will be associated with the events and the sensor. (default = "")
//         */
//        fun setDeviceId(deviceId: String) = apply { config.deviceId = deviceId }

        /**
         * @param wakeLock enable/disable wakelock, permissions needs to be handled by the client.
         */
        fun setWakeLock(wakeLock: Boolean) = apply { config.wakeLockEnabled = wakeLock }

        /**
         * Returns the accelerometer with the built configuration.
         */
        fun build(): Accelerometer = Accelerometer(context, config)

        override fun getConfig(): AccelerometerConfig {
            return config
        }
    }

    override fun start() {
        start(config)
    }

    fun start(config: AccelerometerConfig) {
        this.config = config
        AccelerometerSensor.CONFIG = config

        if (config.wakeLockEnabled and (ContextCompat.checkSelfPermission(context, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED)) {
            Log.e(TAG, "Permission for the WAKE_LOCK is not granted!")
            config.wakeLockEnabled = false
        }

        startService(context)
    }

    override fun stop() {
        stopService(context)
    }

    override fun sync() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEnabled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}