package com.example.deepins

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var mSensorManager:SensorManager
    private lateinit var mAccelerometer :Sensor
    private lateinit var mGyroscope :Sensor
    private lateinit var itemHolder : LinearLayout
    private lateinit var btnOne : Button
    private lateinit var btnTwo : Button
    private lateinit var btnThree : Button


    var interval = 10
    var lastLog = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        itemHolder = findViewById(R.id.records)
        btnOne = findViewById(R.id.btnOne)
        btnTwo = findViewById(R.id.btnTwo)
        btnThree = findViewById(R.id.btnThree)

        btnOne.setOnClickListener {
            interval = 10
        }
        btnTwo.setOnClickListener {
            interval = 100
        }
        btnThree.setOnClickListener {
            interval = 1000
        }
    }
    fun Float.format(digits: Int) = "%.${digits}f".format(this)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    override fun onSensorChanged(event: SensorEvent?) {
        val sensorName: String = event?.sensor!!.getName();
        Log.d("Sensor",sensorName + ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";")
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS")
        val currentDate = sdf.format(Date())

        var log = sensorName + "\n" + currentDate +  "\n X: " + event.values[0].format(2) + "; Y: " + event.values[1].format(2) + "; Z: " + event.values[2].format(2) + ";"

        val inflater = AsyncLayoutInflater(this)

        inflater.inflate(R.layout.record_item, itemHolder) { view, resId, parent ->
            val textMain = view.findViewById<TextView>(R.id.record_item)
            textMain.text = log
            parent!!.addView(view,0)
        }

    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }
}


