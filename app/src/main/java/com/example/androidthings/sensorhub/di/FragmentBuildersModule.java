package com.example.androidthings.sensorhub.di;

import com.example.androidthings.sensorhub.ui.sensor.SensorFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract SensorFragment contributeSensorFragment();
}
