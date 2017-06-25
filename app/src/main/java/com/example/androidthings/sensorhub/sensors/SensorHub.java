package com.example.androidthings.sensorhub.sensors;

import android.arch.lifecycle.MutableLiveData;

import com.example.androidthings.sensorhub.BoardDefaults;
import com.example.androidthings.sensorhub.util.AppExecutors;
import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class SensorHub {

    private static final long SENSOR_READ_INTERVAL_MS = TimeUnit.SECONDS.toMillis(20);
    /**
     * Cutoff to consider a timestamp as valid. Some boards might take some time to update
     * their network time on the first time they boot, and we don't want to publish sensor data
     * with timestamps that we know are invalid. Sensor readings will be ignored until the
     * board's time (System.currentTimeMillis) is more recent than this constant.
     */
    private static final long INITIAL_VALID_TIMESTAMP;
    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 1, 1);
        INITIAL_VALID_TIMESTAMP = calendar.getTimeInMillis();
    }

    public static final String SENSOR_TYPE_MOTION_DETECTION = "motion";
    public static final String SENSOR_TYPE_TEMPERATURE_DETECTION = "temperature";
    public static final String SENSOR_TYPE_AMBIENT_PRESSURE_DETECTION = "ambient_pressure";

    private final AppExecutors appExecutors;

    private Bmx280 bmx280;
    private Button motionDetectorSensor;

    public MutableLiveData<Sensors> sensorData = new MutableLiveData<>();

    @Inject
    public SensorHub(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;

        PeripheralManagerService pioService = new PeripheralManagerService();
        try {
            bmx280 = new Bmx280(BoardDefaults.getI2cBusForSensors(), Bmx280.DEFAULT_I2C_ADDRESS - 1);
            bmx280.setTemperatureOversampling(Bmx280.OVERSAMPLING_1X);
            bmx280.setPressureOversampling(Bmx280.OVERSAMPLING_1X);
            bmx280.setMode(Bmx280.MODE_NORMAL);

            motionDetectorSensor = new Button(BoardDefaults.getGPIOForMotionDetector(),
                    Button.LogicState.PRESSED_WHEN_HIGH);
            //motionDetectorSensor.setOnButtonEventListener(
            //        (button, pressed) -> se.setValue(pressed));
        } catch (Throwable t) {
            Timber.w("Could not initialize BME280 sensor on I2C bus " +
                    BoardDefaults.getI2cBusForSensors(), t);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (bmx280 != null) {
                    try {
                        long now = System.currentTimeMillis();
                        if (now >= INITIAL_VALID_TIMESTAMP) {
                            float[] data = bmx280.readTemperatureAndPressure();
                            appExecutors.mainThread().execute((() -> sensorData.setValue(new Sensors(now, data[0], data[1]))));
                        } else Timber.i("Ignoring sensor readings because timestamp is invalid. " +
                                "Please, set the device's date/time");
                    } catch (Throwable t) {
                        Timber.w("Cannot collect Bmx280 data. Ignoring it for now", t);
                        closeBmx280Quietly();
                    }
                }
            }
        }, 0, SENSOR_READ_INTERVAL_MS);
    }

    private void closeBmx280Quietly() {
        if (bmx280 != null) {
            try {
                bmx280.close();
            } catch (IOException e) {
                Timber.e("Failed to close Bmx280");
            } finally {
                bmx280 = null;
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {

        closeBmx280Quietly();

        if (motionDetectorSensor != null) {
            try {
                motionDetectorSensor.close();
            } catch (IOException e) {
                Timber.e("Failed to close motion sensor");
            } finally {
                motionDetectorSensor = null;
            }
        }

        super.finalize();
    }
}
