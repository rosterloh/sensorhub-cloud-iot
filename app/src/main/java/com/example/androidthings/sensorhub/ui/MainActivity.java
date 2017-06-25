package com.example.androidthings.sensorhub.ui;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.androidthings.sensorhub.R;
import com.example.androidthings.sensorhub.cloud.CloudPublisherService;
import com.example.androidthings.sensorhub.ui.sensor.SensorFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    //private CloudPublisherService mPublishService;
    //private Looper mSensorLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SensorFragment())
                    .commitAllowingStateLoss();
        }
        /*

        initializeServiceIfNeeded();

        // Start the thread that collects sensor data
        HandlerThread thread = new HandlerThread("CloudPublisherService");
        thread.start();
        mSensorLooper = thread.getLooper();

        final Handler sensorHandler = new Handler(mSensorLooper);
        sensorHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    initializeServiceIfNeeded();
                    connectToAvailableSensors();
                    collectContinuousSensors();
                } catch (Throwable t) {
                    Log.e(TAG, String.format(Locale.getDefault(),
                            "Cannot collect sensor data, will try again in %d ms",
                            SENSOR_READ_INTERVAL_MS), t);
                }
                sensorHandler.postDelayed(this, SENSOR_READ_INTERVAL_MS);
            }
        }, SENSOR_READ_INTERVAL_MS);

        connectToAvailableSensors();
        */
    }
/*
    private void initializeServiceIfNeeded() {
        if (mPublishService == null) {
            try {
                // Bind to the service
                Intent intent = new Intent(this, CloudPublisherService.class);
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            } catch (Throwable t) {
                Log.e(TAG, "Could not connect to the service, will try again later", t);
            }
        }
    }

    private void collectContinuousSensors() {
        if (mPublishService != null) {
            List<SensorData> sensorsData = new ArrayList<>();
            addBmx280Readings(sensorsData);
            Log.d(TAG, "collected continuous sensor data: " + sensorsData);
            mPublishService.logSensorData(sensorsData);
        }
    }

    private void addBmx280Readings(List<SensorData> output) {
        if (mEnvironmentalSensor != null) {
            try {
                long now = System.currentTimeMillis();
                if (now >= INITIAL_VALID_TIMESTAMP) {
                    float[] data = mEnvironmentalSensor.readTemperatureAndPressure();
                    output.add(new SensorData(now, SENSOR_TYPE_TEMPERATURE_DETECTION, data[0]));
                    output.add(new SensorData(now, SENSOR_TYPE_AMBIENT_PRESSURE_DETECTION,
                            data[1]));
                } else {
                    Log.i(TAG, "Ignoring sensor readings because timestamp is invalid. " +
                            "Please, set the device's date/time");
                }
            } catch (Throwable t) {
                Log.w(TAG, "Cannot collect Bmx280 data. Ignoring it for now", t);
                closeBmx280Quietly();
            }
        }
    }

    private void collectSensorOnChange(String type, float sensorReading) {
        if (mPublishService != null) {
            Log.d(TAG, "On change " + type + ": " + sensorReading);
            long now = System.currentTimeMillis();
            if (now >= INITIAL_VALID_TIMESTAMP) {
                mPublishService.logSensorDataOnChange(new SensorData(now, type, sensorReading));
            } else {
                Log.i(TAG, "Ignoring sensor readings because timestamp is invalid. " +
                        "Please, set the device's date/time");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSensorLooper.quit();
        closeBmx280Quietly();
        closeMotionDetectorQuietly();

        // unbind from Cloud Publisher service.
        if (mPublishService != null) {
            unbindService(mServiceConnection);
        }
    }
*/

    /**
     * Callback for service binding, passed to bindService()
     *//*
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            CloudPublisherService.LocalBinder binder = (CloudPublisherService.LocalBinder) service;
            mPublishService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mPublishService = null;
        }
    };*/

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
