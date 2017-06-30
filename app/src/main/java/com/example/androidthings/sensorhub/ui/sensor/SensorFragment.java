package com.example.androidthings.sensorhub.ui.sensor;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidthings.sensorhub.R;
import com.example.androidthings.sensorhub.databinding.SensorFragmentBinding;
import com.example.androidthings.sensorhub.sensors.Sensors;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SensorFragment extends LifecycleFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    SensorFragmentBinding binding;

    private SensorViewModel sensorViewModel;

    private LineChart lineChart;

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

        initGraphView();

        sensorViewModel.getSensorData().observe(this, sensors -> {
            addEntry(sensors);
            binding.setSensors(sensors);
        });
    }

    public void initGraphView() {
        lineChart = binding.graph;
        lineChart.getDescription().setEnabled(false);

        LineData lineData = new LineData();
        lineData.setValueTextColor(Color.WHITE);

        // add empty data
        lineChart.setData(lineData);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.LTGRAY);

        XAxis xl = lineChart.getXAxis();
        xl.setTextColor(Color.LTGRAY);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.LTGRAY);
        //leftAxis.setAxisMaximum(40f);
        //leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTextColor(Color.LTGRAY);
        //rightAxis.setAxisMaximum(100f);
        //rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawGridLines(false);
    }

    private void addEntry(Sensors value) {
        LineData data = lineChart.getLineData();

        if (data != null) {

            ILineDataSet tempSet = data.getDataSetByIndex(0);
             if (tempSet == null) {
                 tempSet = createTempSet();
                data.addDataSet(tempSet);
            }

            ILineDataSet humSet = data.getDataSetByIndex(1);
            if (humSet == null) {
                humSet = createHumSet();
                data.addDataSet(humSet);
            }

            data.addEntry(new Entry(tempSet.getEntryCount(), value.temperature), 0);
            data.addEntry(new Entry(humSet.getEntryCount(), value.pressure), 1);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            lineChart.notifyDataSetChanged();

            // limit the number of visible entries
            lineChart.setVisibleXRangeMaximum(120);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            lineChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createTempSet() {

        LineDataSet set = new LineDataSet(null, "Temperature");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.MATERIAL_COLORS[0]);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(1f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.MATERIAL_COLORS[0]);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private LineDataSet createHumSet() {

        LineDataSet set = new LineDataSet(null, "Humidity");
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setColor(ColorTemplate.MATERIAL_COLORS[1]);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(1f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.MATERIAL_COLORS[1]);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
}
