package com.example.androidthings.sensorhub.ui.sensor;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidthings.sensorhub.R;
import com.example.androidthings.sensorhub.databinding.SensorFragmentBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SensorFragment extends LifecycleFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    SensorFragmentBinding binding;

    private SensorViewModel sensorViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.sensor_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onActivityCreated(savedInstanceState);
        sensorViewModel = ViewModelProviders.of(this, viewModelFactory).get(SensorViewModel.class);
        sensorViewModel.getSensorData().observe(this, sensors -> binding.setSensors(sensors));
    }
}
