package com.example.healthfirst;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SatVegFood extends AppCompatActivity {
    public RecyclerView recyclerView;
    public ArrayList<Modal> datalist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sat_veg_food);
        recyclerView=findViewById(R.id.rv);

        datalist=new ArrayList<>();
        datalist.add(new Modal(R.drawable.avocado2,"Quả bơ"));
        datalist.add(new Modal(R.drawable.apricot1,"Quả mơ"));
        datalist.add(new Modal(R.drawable.fig1,"Sung"));
        datalist.add(new Modal(R.drawable.star1,"Quả khế"));
        //datalist.add(new Modal(R.drawable.mobile));




        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this,datalist);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}