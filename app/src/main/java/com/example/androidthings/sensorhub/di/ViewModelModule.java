package com.example.androidthings.sensorhub.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.androidthings.sensorhub.ui.sensor.SensorViewModel;
import com.example.androidthings.sensorhub.viewmodel.SensorHubViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SensorViewModel.class)
    abstract ViewModel bindSensorViewModel(SensorViewModel sensorViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(SensorHubViewModelFactory factory);
}
