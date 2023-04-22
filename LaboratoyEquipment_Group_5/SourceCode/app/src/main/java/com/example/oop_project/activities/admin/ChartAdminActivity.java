package com.example.oop_project.activities.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oop_project.databinding.ActivityChartAdminBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChartAdminActivity extends AppCompatActivity {
    private ActivityChartAdminBinding binding;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<Pair<String, String>> listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        barEntries = new ArrayList<>();
        listView = new ArrayList<>();

        binding.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLabel();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.barChart.setDrawBarShadow(false);
        binding.barChart.setDrawValueAboveBar(true);
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setPinchZoom(false);
        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setExtraTopOffset(10f);
        loadBarChartMostViewed();



    }

    private void loadBarChartMostViewed() {
        barEntries.clear();
        listView.clear();;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barEntries.clear();
                listView.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String title = "" + ds.child("title").getValue();
                    String viewed = "" + ds.child("viewed").getValue();

                    Pair<String, String> p = new Pair<>(title, viewed);
                    listView.add(p);
                }

                Collections.sort(listView, new Comparator<Pair<String, String>>() {
                    @Override
                    public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                        // Chuyển đổi giá trị số lần view sang kiểu số nguyên để so sánh
                        int viewed1 = Integer.parseInt(o1.second);
                        int viewed2 = Integer.parseInt(o2.second);
                        // Sắp xếp theo thứ tự giảm dần
                        return Integer.compare(viewed2, viewed1);
                    }
                });
                List<Pair<String, String>> firstFiveEntries = listView.subList(0, Math.min(5, listView.size()));
                ArrayList<String> titleE = new ArrayList<>();
                for(int i = 0; i < firstFiveEntries.size(); i++){
                    String title = firstFiveEntries.get(i).first;
                    String viewed = firstFiveEntries.get(i).second;
                    titleE.add(title);
                    barEntries.add(new BarEntry(i, Integer.parseInt(viewed)));
                }
                BarDataSet barDataSet = new BarDataSet(barEntries, "Số lần đã xem");
                int[] colors = new int[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA};

                barDataSet.setColors(colors); // Đặt màu cho cột
                barDataSet.setValueTextSize(12f); // Đặt kích thước chữ của giá trị trên cột

                BarData barData = new BarData(barDataSet);

                ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(titleE); // Định d
                binding.barChart.getXAxis().setValueFormatter(xAxisFormatter);
                binding.barChart.setData(barData);

                XAxis xAxis = binding.barChart.getXAxis();
                xAxis.setTextSize(14f);
                YAxis yAxis = binding.barChart.getAxisLeft();
                yAxis.setTextSize(14f);
                binding.barChart.getData().setValueTextSize(12f);


                binding.barChart.invalidate();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void loadBarChartMostBorrowed() {
        barEntries.clear();
        listView.clear();;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barEntries.clear();
                listView.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String title = "" + ds.child("title").getValue();
                    String viewed = "" + ds.child("numberOfBorrowings").getValue();

                    Pair<String, String> p = new Pair<>(title, viewed);
                    listView.add(p);
                }

                Collections.sort(listView, new Comparator<Pair<String, String>>() {
                    @Override
                    public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                        // Chuyển đổi giá trị số lần view sang kiểu số nguyên để so sánh
                        int viewed1 = Integer.parseInt(o1.second);
                        int viewed2 = Integer.parseInt(o2.second);
                        // Sắp xếp theo thứ tự giảm dần
                        return Integer.compare(viewed2, viewed1);
                    }
                });
                List<Pair<String, String>> firstFiveEntries = listView.subList(0, Math.min(5, listView.size()));
                ArrayList<String> titleE = new ArrayList<>();
                for(int i = 0; i < firstFiveEntries.size(); i++){
                    String title = firstFiveEntries.get(i).first;
                    String viewed = firstFiveEntries.get(i).second;
                    titleE.add(title);
                    barEntries.add(new BarEntry(i, Integer.parseInt(viewed)));
                }
                BarDataSet barDataSet = new BarDataSet(barEntries, "Số lần mượn ");
                int[] colors = new int[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA};

                barDataSet.setColors(colors); // Đặt màu cho cột
                barDataSet.setValueTextSize(12f); // Đặt kích thước chữ của giá trị trên cột

                BarData barData = new BarData(barDataSet);

                ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(titleE); // Định d
                binding.barChart.getXAxis().setValueFormatter(xAxisFormatter);
                binding.barChart.setData(barData);

                XAxis xAxis = binding.barChart.getXAxis();
                xAxis.setTextSize(14f);
                YAxis yAxis = binding.barChart.getAxisLeft();
                yAxis.setTextSize(14f);
                binding.barChart.getData().setValueTextSize(12f);


                binding.barChart.invalidate();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void showLabel() {
        PopupMenu popupMenu = new PopupMenu(this, binding.label);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Viewed");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Borrowed");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // get id of item clicked
                int which = item.getItemId();
                if (which == 0) {
                    binding.label.setText("Viewed");
                    loadBarChartMostViewed();
                } else if (which == 1) {
                    binding.label.setText("Borrowed");
                    loadBarChartMostBorrowed();
                }
                return false;
            }
        });
    }


}