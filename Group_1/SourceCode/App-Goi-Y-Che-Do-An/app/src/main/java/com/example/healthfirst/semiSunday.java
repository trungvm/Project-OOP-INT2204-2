package com.example.healthfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class semiSunday extends AppCompatActivity {

    PieChart pieChart;
    Button b14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semi_sunday);



        //NO TITLE
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        //NO TITLE

        pieChart = (PieChart) findViewById(R.id.piechart);

        setupPieChart();
        loadPieChartData();
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Vitamins & Chất khoáng");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(0.24f,"kali"));
        entries.add(new PieEntry(0.036f,"Sắt"));
        entries.add(new PieEntry(0.04f,"Vitamin K"));
        entries.add(new PieEntry(0.55f,"Vitamin B6"));
        entries.add(new PieEntry(0.14f,"Đường"));
        entries.add(new PieEntry(0.31f,"Chất xơ"));
        entries.add(new PieEntry(0.44f,"Kẽm"));


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Vitamins");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
        b14=findViewById(R.id.b14);
        b14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SunSemiVegFood.class);
                startActivity(i);

            }
        });
    }
}