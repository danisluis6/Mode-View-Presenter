package com.example.enclaveit.app.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by PHA on 07/03/2017.
 * https://www.studytutorial.in/android-bar-chart-or-bar-graph-using-mpandroid-library-tutorial
 * https://github.com/PhilJay/MPAndroidChart
 */

public class FragmentChartReport extends Fragment {
    Random rd;
    private final String[] listSubjects = {"Art", "Biology", "Chemistry", "English", "Geography", "Health", "Logic",
            "Mathematics", "Music", "Philosophy", "Physics", "Programming", "Reading","Science", "Sports"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.chart_report_fragment_layout, container, false);
        BarChart barChart = (BarChart) view.findViewById(R.id.barChart);
        int[] bgEvaluation = {getContext().getResources().getColor(R.color.excellent),
                                getContext().getResources().getColor(R.color.good),
                                getContext().getResources().getColor(R.color.normal),
                                getContext().getResources().getColor(R.color.improve),
                                getContext().getResources().getColor(R.color.bad)};
        rd = new Random();

        //Define Subject Labels
        ArrayList<String> subjectLabels = new ArrayList<>();
            for (String subject : listSubjects){
                subjectLabels.add(subject);
            }
        //Create Bar Entries for Chart
        ArrayList<BarEntry> entriesExcellent = new ArrayList<>();
        for (int i=0; i<listSubjects.length; i++){
            BarEntry subject;
            float value = rd.nextFloat()*10;
            Log.i("CHART_REPORT", "Random: "+value);
            subject = new BarEntry(value, i);
            entriesExcellent.add(subject);
        }
        //Initialize all the Bar Chart for each Evaluation
        MyBarDataSet myBarDataSet = new MyBarDataSet(entriesExcellent, "");
        myBarDataSet.setColors(bgEvaluation);   //set Color for each column based on value. Watch more at getColor() inMyBarDataSet
        myBarDataSet.setBarSpacePercent(40.0f); //Set Space between columns
        myBarDataSet.setValueTextColor(getResources().getColor(R.color.colorAccent));
        myBarDataSet.setValueTextSize(13f); //Set textSize for each Column data

        //Load data to BarChart
        BarData barData = new BarData(subjectLabels, myBarDataSet);
            barChart.setData(barData);
            barChart.animateY(2000);
            barChart.setMaxVisibleValueCount(10);
            barChart.setVisibleXRangeMaximum(4);   // allow 4 values to be displayed at once on the x-axis, not more
            barChart.setBackgroundColor(getResources().getColor(R.color.bg_none));
            // scaling can now only be done on x- and y-axis separately
            barChart.setPinchZoom(false);
            barChart.setDrawGridBackground(false);
            barChart.setDoubleTapToZoomEnabled(false);

        /*Change color of Label*/
        XAxis xLabel = barChart.getXAxis();
            xLabel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            xLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            xLabel.setPosition(XAxis.XAxisPosition.BOTTOM); //Set Subject Name at the bottom
        YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setAxisMaxValue(10);  //set Max Value for YAxis Left
        YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setEnabled(false);    //Disable YAxis Right
        //Disable legend at bottom
            barChart.getLegend().setEnabled(false);
        barChart.setDescription("");

        barChart.invalidate();

        return view;
    }
}
